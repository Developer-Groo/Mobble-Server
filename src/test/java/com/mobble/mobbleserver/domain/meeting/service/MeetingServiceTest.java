package com.mobble.mobbleserver.domain.meeting.service;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.meeting.dto.request.MeetingRequestDto;
import com.mobble.mobbleserver.domain.meeting.dto.response.MeetingResponseDto;
import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.entity.MeetingType;
import com.mobble.mobbleserver.domain.meeting.repository.MeetingRepository;
import com.mobble.mobbleserver.domain.meeting.validator.MeetingValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.security.SecurityErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
}
