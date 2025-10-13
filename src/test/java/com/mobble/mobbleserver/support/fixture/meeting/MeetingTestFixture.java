package com.mobble.mobbleserver.support.fixture.meeting;

import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.meeting.entity.Meeting;
import com.mobble.mobbleserver.refactor.meeting.entity.MeetingType;

import java.time.LocalDateTime;

public class MeetingTestFixture {

    public static Meeting createDefaultMeeting(ClubMember clubMember) {
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
