package com.mobble.mobbleserver.domain.meetingMember.entity;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.member.entity.Member;
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
}
