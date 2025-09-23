package com.mobble.mobbleserver.domain.meetingMember.entity;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingMemberErrorCode;
import com.mobble.mobbleserver.support.fixture.meeting.MeetingTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MeetingMemberTest {

    @Test
    @DisplayName("Meeting Member 생성 성공")
    void success_create_meeting_member() {
        // given
        Meeting meeting = MeetingTestFixture.createDefaultMeeting();
        Member member = MemberTestFixture.createDefaultMember();

        // when
        MeetingMember meetingMember = MeetingMember.createMeetingMember(meeting, member);

        // then
        assertThat(meetingMember).isNotNull();
        assertThat(meetingMember.getMeeting()).isEqualTo(meeting);
        assertThat(meetingMember.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("Meeting 이 null 인 경우 예외 발생")
    void fail_when_meeting_is_null() {
        // given
        Member member = MemberTestFixture.createDefaultMember();

        // when & then
        assertThatThrownBy(() -> MeetingMember.createMeetingMember(null, member))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingMemberErrorCode.MEETING_REQUIRED.message());
    }
    
    @Test
    @DisplayName("Member 가 null 인 경우 예외 발생")
    void fail_when_member_is_null() {
        // given
        Meeting meeting = MeetingTestFixture.createDefaultMeeting();

        // when & then
        assertThatThrownBy(() -> MeetingMember.createMeetingMember(meeting, null))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingMemberErrorCode.MEMBER_REQUIRED.message());
    }
}
