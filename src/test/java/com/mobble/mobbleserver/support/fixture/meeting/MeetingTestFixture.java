package com.mobble.mobbleserver.support.fixture.meeting;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.entity.MeetingType;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;

import java.time.LocalDateTime;

public class MeetingTestFixture {

    public static Meeting createDefaultMeeting(ClubMember clubMember) {
        Member member = MemberTestFixture.createDefaultMember();
        ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(clubCategory);

        return Meeting.createMeeting(
                clubMember,
                "정기 모임",
                LocalDateTime.of(2025, 10, 10, 19, 0),
                "체육관",
                "5000",
                10,
                MeetingType.REGULAR_MEETING
        );
    }
}
