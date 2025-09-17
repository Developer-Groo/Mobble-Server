package com.mobble.mobbleserver.domain.meeting.service;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingUpdateRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.response.MeetingResponseDto;
import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.entity.MeetingType;
import com.mobble.mobbleserver.domain.meeting.repository.MeetingRepository;
import com.mobble.mobbleserver.domain.meeting.validator.MeetingValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.security.SecurityErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private MeetingValidator meetingValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @InjectMocks
    private MeetingService meetingService;

    private static final Long CLUB_ID = 1L;
    private static final Long MEMBER_ID = 2L;
    private static final Long MEETING_ID = 3L;

    @Nested
    @DisplayName("Meeting 생성")
    class CreateMeeting {

        @Test
        @DisplayName("Meeting 생성 성공 - LEADER")
        void success_when_create_meeting() {
            // given
            ClubMember leader = mock(ClubMember.class);
            given(leader.getClubMemberRole()).willReturn(ClubMemberRole.LEADER);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(leader);

            Club mockClub = mock(Club.class);
            given(leader.getClub()).willReturn(mockClub);
            given(mockClub.getId()).willReturn(CLUB_ID);

            MeetingRequestDto dto = new MeetingRequestDto(
                    "title",
                    LocalDateTime.of(2025, 10, 10, 19, 0),
                    "체육관",
                    "5000",
                    10,
                    MeetingType.REGULAR_MEETING
            );

            Meeting savedMeeting = mock(Meeting.class);
            given(savedMeeting.getDatetime()).willReturn(dto.dateTime());
            given(savedMeeting.getClubMember()).willReturn(leader);

            given(meetingRepository.save(any(Meeting.class))).willReturn(savedMeeting);

            // when
            MeetingResponseDto meeting = meetingService.createMeeting(MEMBER_ID, CLUB_ID, dto);

            // then
            assertThat(meeting).isNotNull();
            verify(meetingRepository).save(any());
        }

        @Test
        @DisplayName("Meeting 생성 실패 - MEMBER")
        void fail_when_create_meeting_by_member() {
            // given
            ClubMember member = mock(ClubMember.class);
            given(member.getClubMemberRole()).willReturn(ClubMemberRole.MEMBER);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(member);

            MeetingRequestDto dto = new MeetingRequestDto(
                    "title",
                    LocalDateTime.of(2025, 10, 10, 19, 0),
                    "체육관",
                    "5000",
                    10,
                    MeetingType.REGULAR_MEETING
            );

            // when & then
            assertThatThrownBy(() -> meetingService.createMeeting(MEMBER_ID, CLUB_ID, dto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(SecurityErrorCode.ACCESS_DENIED.message());

            verify(clubMemberValidator).findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID);
            verify(meetingRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Meeting 수정")
    class UpdateMeeting {

        @Test
        @DisplayName("Meeting 수정 성공 - LEADER")
        void success_when_update_meeting() {
            // given
            ClubMember leader = mock(ClubMember.class);
            Club club = mock(Club.class);
            given(leader.getClubMemberRole()).willReturn(ClubMemberRole.LEADER);
            given(leader.getClub()).willReturn(club);
            given(club.getId()).willReturn(CLUB_ID);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(leader);

            Meeting meeting = mock(Meeting.class);
            given(meeting.getMeetingMembers()).willReturn(List.of());
            given(meeting.getDatetime()).willReturn(LocalDateTime.of(2025, 10, 10, 20, 0));
            given(meeting.getClubMember()).willReturn(leader);
            given(meetingRepository.save(any())).willReturn(meeting);
            given(meetingValidator.findMeetingByMeetingIdOrThrow(MEETING_ID)).willReturn(meeting);

            MeetingUpdateRequestDto dto = new MeetingUpdateRequestDto(
                    "updated title",
                    LocalDateTime.of(2025, 10, 10, 20, 0),
                    "체육관 2",
                    "60000",
                    20,
                    MeetingType.IMPROMPTU_MEETING
            );

            // when
            MeetingResponseDto updateMeeting = meetingService.updateMeeting(MEMBER_ID, CLUB_ID, MEETING_ID, dto);

            assertThat(updateMeeting).isNotNull();
            verify(meeting).updateMeeting(
                    dto.title(),
                    dto.dateTime(),
                    dto.location(),
                    dto.cost(),
                    dto.memberLimit(),
                    dto.type()
            );
            verify(meetingRepository).save(meeting);
        }

        @Test
        @DisplayName("Meeting 수정 실패 - MEMBER")
        void fail_when_update_meeting_by_member() {
            // given
            ClubMember member = mock(ClubMember.class);
            given(member.getClubMemberRole()).willReturn(ClubMemberRole.MEMBER);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(member);

            MeetingUpdateRequestDto dto = new MeetingUpdateRequestDto(
                    "updated title",
                    LocalDateTime.of(2025, 10, 10, 20, 0),
                    "체육관 2",
                    "60000",
                    20,
                    MeetingType.IMPROMPTU_MEETING
            );

            // when & then
            assertThatThrownBy(() -> meetingService.updateMeeting(MEMBER_ID, CLUB_ID, MEETING_ID, dto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(SecurityErrorCode.ACCESS_DENIED.message());

            verify(clubMemberValidator).findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID);
            verify(meetingValidator, never()).findMeetingByMeetingIdOrThrow(any());
            verify(meetingRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Meeting 목록 조회")
    class FindMeeting {

        @Test
        @DisplayName("Meeting 목록 조회 성공")
        void success_when_find_meetings() {
            // given
            ClubMember clubMember = mock(ClubMember.class);
            Club club = mock(Club.class);
            given(club.getId()).willReturn(CLUB_ID);
            given(clubMember.getClub()).willReturn(club);

            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(clubMember);

            Meeting meeting1 = mock(Meeting.class);
            given(meeting1.getMeetingMembers()).willReturn(List.of());
            given(meeting1.getDatetime()).willReturn(LocalDateTime.of(2025, 10, 10, 20, 0));
            given(meeting1.getClubMember()).willReturn(clubMember);

            Meeting meeting2 = mock(Meeting.class);
            given(meeting2.getMeetingMembers()).willReturn(List.of());
            given(meeting2.getDatetime()).willReturn(LocalDateTime.of(2025, 10, 20, 20, 0));
            given(meeting2.getClubMember()).willReturn(clubMember);

            given(meetingValidator.findMeetingsByClubId(CLUB_ID)).willReturn(List.of(meeting1, meeting2));

            // when
            List<MeetingResponseDto> result = meetingService.findMeetingsByClubId(MEMBER_ID, CLUB_ID);

            // then
            assertThat(result).hasSize(2);
            verify(meetingValidator).findMeetingsByClubId(CLUB_ID);
        }

        @Test
        @DisplayName("Meeting 목록 조회 실패 - Club 미가입")
        void fail_when_not_club_member() {
            // given
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willThrow(new DomainException(SecurityErrorCode.ACCESS_DENIED));

            // when & then
            assertThatThrownBy(() -> meetingService.findMeetingsByClubId(MEMBER_ID, CLUB_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(SecurityErrorCode.ACCESS_DENIED.message());

            verify(clubMemberValidator).findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID);
            verify(meetingValidator, never()).findMeetingsByClubId(any());
        }
    }
}
