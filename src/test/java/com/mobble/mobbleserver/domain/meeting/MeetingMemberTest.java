package com.mobble.mobbleserver.domain.meeting;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.support.fixture.meeting.MeetingTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class MeetingMemberTest {

    private final Club mockClub = mock(Club.class);
    private final Member mockMember = mock(Member.class);
    private final Image mockMainImage = mock(Image.class);

    @Nested
    class Create {

        @Test
        void success_create() {
            Meeting meeting = MeetingTestFixture.createDefaultMeeting(mockClub, mockMember, mockMainImage);
            Member member = MemberTestFixture.createDefaultMember();

            MeetingMember meetingMember = MeetingMember.createMeetingMember(meeting, member);

            assertThat(meetingMember.getMeeting()).isEqualTo(meeting);
            assertThat(meetingMember.getMember()).isEqualTo(member);
        }

        @Test
        void success_fail_when_meeting_null() {
            Member member = MemberTestFixture.createDefaultMember();

            assertThatThrownBy(() -> MeetingMember.createMeetingMember(null, member))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("meeting must not be null");
        }

        @Test
        void success_fail_when_member_null() {
            Meeting meeting = MeetingTestFixture.createDefaultMeeting(mockClub, mockMember, mockMainImage);

            assertThatThrownBy(() -> MeetingMember.createMeetingMember(meeting, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("member must not be null");
        }
    }

    @Test
    void success_detach() {
        Meeting meeting = MeetingTestFixture.createDefaultMeeting(mockClub, mockMember, mockMainImage);
        Member member = MemberTestFixture.createDefaultMember();

        MeetingMember meetingMember = MeetingMember.createMeetingMember(meeting, member);

        meetingMember.detach();

        assertThat(meetingMember.getMeeting()).isNull();
    }
}
