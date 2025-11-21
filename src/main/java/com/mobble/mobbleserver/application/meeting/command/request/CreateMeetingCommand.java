package com.mobble.mobbleserver.application.meeting.command.request;

import com.mobble.mobbleserver.domain.meeting.MeetingType;

import java.time.LocalDateTime;

public record CreateMeetingCommand(
        Long memberId,
        Long clubId,
        String title,
        LocalDateTime schedule,
        String location,
        String cost,
        Integer memberLimit,
        MeetingType type
) {
    
    public static CreateMeetingCommand create(
            Long memberId,
            Long clubId,
            String title,
            LocalDateTime schedule,
            String location,
            String cost,
            Integer memberLimit,
            MeetingType type
    ) {
        return new CreateMeetingCommand(memberId, clubId, title, schedule, location, cost, memberLimit, type);
    }
}
