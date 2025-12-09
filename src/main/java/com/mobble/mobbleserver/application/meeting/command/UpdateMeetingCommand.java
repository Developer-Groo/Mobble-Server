package com.mobble.mobbleserver.application.meeting.command;

import com.mobble.mobbleserver.domain.meeting.MeetingType;

import java.time.LocalDateTime;

public record UpdateMeetingCommand(
        Long memberId,
        Long clubId,
        Long meetingId,
        String title,
        Long mainImageId,
        LocalDateTime schedule,
        String location,
        String cost,
        Integer memberLimit,
        MeetingType type
) {

    public static UpdateMeetingCommand create(
            Long memberId,
            Long clubId,
            Long meetingId,
            String title,
            Long mainImageId,
            LocalDateTime schedule,
            String location,
            String cost,
            Integer memberLimit,
            MeetingType type
    ) {
        return new UpdateMeetingCommand(
                memberId,
                clubId,
                meetingId,
                title,
                mainImageId,
                schedule,
                location,
                cost,
                memberLimit,
                type
        );
    }
}
