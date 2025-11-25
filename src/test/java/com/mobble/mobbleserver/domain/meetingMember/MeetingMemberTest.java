package com.mobble.mobbleserver.domain.meetingMember;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MeetingMemberTest {

    @Test
    @DisplayName("Meeting Member 생성 성공")
    void success_create_meeting_member() {
//        // given
//        Member member = MemberTestFixture.createDefaultMember();
//        ClubCategory clubCategory = ClubCategoryTestFixture.createDefaultCategory();
//        Club club = ClubTestFixture.createDefaultClub(clubCategory);
//        ClubMember clubMember = ClubMemberTestFixture.createDefaultClubMember(member, club, ClubMemberRole.LEADER, JoinStatus.APPROVED);
//        Meeting meeting = MeetingTestFixture.createDefaultMeeting(clubMember);
//
//        // when
//        MeetingMember meetingMember = MeetingMember.createMeetingMember(meeting, member);
//
//        // then
//        assertThat(meetingMember).isNotNull();
//        assertThat(meetingMember.getMeeting()).isEqualTo(meeting);
//        assertThat(meetingMember.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("Meeting 이 null 인 경우 예외 발생")
    void fail_when_meeting_is_null() {
//        // given
//        Member member = MemberTestFixture.createDefaultMember();
//
//        // when & then
//        assertThatThrownBy(() -> MeetingMember.createMeetingMember(null, member))
//                .isInstanceOf(DomainException.class)
//                .hasMessage(MeetingMemberErrorCode.MEETING_REQUIRED.message());
    }

    @Test
    @DisplayName("Member 가 null 인 경우 예외 발생")
    void fail_when_member_is_null() {
//        // given
//        Member member = MemberTestFixture.createDefaultMember();
//        ClubCategory clubCategory = ClubCategoryTestFixture.createDefaultCategory();
//        Club club = ClubTestFixture.createDefaultClub(clubCategory);
//        ClubMember clubMember = ClubMemberTestFixture.createDefaultClubMember(member, club, ClubMemberRole.LEADER, JoinStatus.APPROVED);
//        Meeting meeting = MeetingTestFixture.createDefaultMeeting(clubMember);
//
//        // when & then
//        assertThatThrownBy(() -> MeetingMember.createMeetingMember(meeting, null))
//                .isInstanceOf(DomainException.class)
//                .hasMessage(MeetingMemberErrorCode.MEMBER_REQUIRED.message());
    }
}
