package com.mobble.mobbleserver.refactor.meetingMember.service;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingMemberErrorCode;
import com.mobble.mobbleserver.refactor.meeting.entity.Meeting;
import com.mobble.mobbleserver.refactor.meeting.validator.MeetingValidator;
import com.mobble.mobbleserver.refactor.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.refactor.meetingMember.dto.response.MeetingMemberListResponseDto;
import com.mobble.mobbleserver.refactor.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.refactor.meetingMember.repository.MeetingMemberRepository;
import com.mobble.mobbleserver.refactor.meetingMember.validator.MeetingMemberValidator;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.member.validator.MemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MeetingMemberServiceTest {

    @Mock
    private MeetingMemberRepository meetingMemberRepository;

    @Mock
    private MeetingValidator meetingValidator;

    @Mock
    private MeetingMemberValidator meetingMemberValidator;

    @Mock
    private MemberValidator memberValidator;

    @InjectMocks
    private MeetingMemberService meetingMemberService;

    private static final Long MEETING_ID = 1L;
    private static final Long MEMBER_ID = 10L;

    private Meeting meeting;
    private Member member;

    @BeforeEach
    void setUp() {
        meeting = mock(Meeting.class);
        member = mock(Member.class);
    }

    @Nested
    @DisplayName("attendMeeting")
    class AttendMeeting {

        @Test
        @DisplayName("참석 성공")
        void success_attend_meeting() {
            // given
            given(meetingValidator.findMeetingByMeetingIdOrThrow(MEETING_ID)).willReturn(meeting);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(member);
            given(meetingMemberValidator.findMeetingByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID)).willReturn(Optional.empty());
            given(meetingMemberRepository.countByMeetingId(MEETING_ID)).willReturn(2);

            given(meeting.getId()).willReturn(MEETING_ID);
            given(meeting.getMemberLimit()).willReturn(5);
            given(member.getId()).willReturn(MEMBER_ID);

            // when
            MeetingAttendanceResponseDto response = meetingMemberService.attendMeeting(MEETING_ID, MEMBER_ID);

            // then
            verify(meetingMemberRepository).save(any(MeetingMember.class));
            assertThat(response.meetingId()).isEqualTo(MEETING_ID);
            assertThat(response.isAttending()).isTrue();
        }

        @Test
        @DisplayName("이미 참석한 경우 참석 취소")
        void success_cancel_attend_meeting() {
            // given
            MeetingMember attending = mock(MeetingMember.class);

            given(meetingValidator.findMeetingByMeetingIdOrThrow(MEETING_ID)).willReturn(meeting);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(member);
            given(meetingMemberValidator.findMeetingByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID)).willReturn(Optional.of(attending));

            given(meeting.getId()).willReturn(MEETING_ID);
            given(member.getId()).willReturn(MEMBER_ID);

            // when
            MeetingAttendanceResponseDto response = meetingMemberService.attendMeeting(MEETING_ID, MEMBER_ID);

            // then
            verify(meetingMemberRepository).delete(attending);
            assertThat(response.meetingId()).isEqualTo(MEETING_ID);
            assertThat(response.isAttending()).isFalse();
        }

        @Test
        @DisplayName("정원 초과 시 예외 발생")
        void throw_exception_when_capacity_is_full() {
            // given
            given(meetingValidator.findMeetingByMeetingIdOrThrow(MEETING_ID)).willReturn(meeting);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(member);
            given(meetingMemberValidator.findMeetingByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID)).willReturn(Optional.empty());
            given(meetingMemberRepository.countByMeetingId(MEETING_ID)).willReturn(5);

            given(meeting.getId()).willReturn(MEETING_ID);
            given(meeting.getMemberLimit()).willReturn(5);
            given(member.getId()).willReturn(MEMBER_ID);

            // when & then
            assertThatThrownBy(() -> meetingMemberService.attendMeeting(MEETING_ID, MEMBER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MeetingMemberErrorCode.FULL_CAPACITY.message());
        }

        @Test
        @DisplayName("Meeting 이 존재하지 않을 경우 예외 발생")
        void fail_when_not_found_meeting() {
            // given
            given(meetingValidator.findMeetingByMeetingIdOrThrow(MEETING_ID)).willThrow(new DomainException(MeetingErrorCode.NOT_FOUND_MEETING));

            // when & then
            assertThatThrownBy(() -> meetingMemberService.attendMeeting(MEETING_ID, MEMBER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(MeetingErrorCode.NOT_FOUND_MEETING.message());
        }
    }

    @Nested
    @DisplayName("getMeetingMembers")
    class GetMeetingMembers {

        @Test
        @DisplayName("미팅 참석자 목록 조회 성공")
        void success_get_meeting_members() {
            // given
            Member member1 = mock(Member.class);
            Member member2 = mock(Member.class);
            given(member1.getId()).willReturn(100L);
            given(member2.getId()).willReturn(101L);

            MeetingMember meetingMember1 = MeetingMember.createMeetingMember(meeting, member1);
            MeetingMember meetingMember2 = MeetingMember.createMeetingMember(meeting, member2);

            List<MeetingMember> meetingMembers = List.of(meetingMember1, meetingMember2);

            given(meetingValidator.findMeetingByMeetingIdOrThrow(MEETING_ID)).willReturn(meeting);
            given(meeting.getId()).willReturn(MEETING_ID);
            given(meetingMemberValidator.findByMeetingId(MEETING_ID)).willReturn(meetingMembers);

            // when
            MeetingMemberListResponseDto response = meetingMemberService.getMeetingMembers(MEETING_ID);

            // then
            assertThat(response.meetingId()).isEqualTo(MEETING_ID);
            assertThat(response.meetingMembers()).hasSize(2);
        }

        @Test
        @DisplayName("미팅 참석자가 없으면 빈 리스트 반환")
        void success_return_empty_list_when_no_attendees() {
            // given
            given(meetingValidator.findMeetingByMeetingIdOrThrow(MEETING_ID)).willReturn(meeting);
            given(meeting.getId()).willReturn(MEETING_ID);
            given(meetingMemberValidator.findByMeetingId(MEETING_ID)).willReturn(List.of());

            // when
            MeetingMemberListResponseDto response = meetingMemberService.getMeetingMembers(MEETING_ID);

            // then
            assertThat(response.meetingId()).isEqualTo(MEETING_ID);
            assertThat(response.meetingMembers()).isEmpty();
        }
    }
}
