package com.mobble.mobbleserver.support.fixture.meeting;

import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.mobble.mobbleserver.domain.meeting.MeetingSchedule;
import com.mobble.mobbleserver.domain.meeting.MeetingType;
import com.mobble.mobbleserver.domain.member.Member;

import java.time.LocalDateTime;

public class MeetingTestFixture {

    public static Meeting createDefaultMeeting(Club club, Member owner, Image mainImage) {
        return Meeting.create(
                club,
                owner,
                "title",
                mainImage,
                MeetingSchedule.of(LocalDateTime.now().plusDays(1)),
                "location",
                "5000",
                10,
                MeetingType.REGULAR_MEETING
        );
    }
}
