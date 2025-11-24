package com.mobble.mobbleserver.support.fixture.meeting;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingType;

import java.time.LocalDateTime;

public class MeetingTestFixture {

    public static Meeting createDefaultMeeting(ClubMember clubMember) {
        return Meeting.create(
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
