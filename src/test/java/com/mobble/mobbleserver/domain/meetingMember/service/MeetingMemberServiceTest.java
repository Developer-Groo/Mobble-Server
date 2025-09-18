package com.mobble.mobbleserver.domain.meetingMember.service;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.validator.MeetingValidator;
import com.mobble.mobbleserver.domain.meetingMember.dto.response.MeetingAttendanceResponseDto;
import com.mobble.mobbleserver.domain.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.domain.meetingMember.repository.MeetingMemberRepository;
import com.mobble.mobbleserver.domain.meetingMember.validator.MeetingMemberValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingMemberErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    class AttendMeeting{

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
    }
}
