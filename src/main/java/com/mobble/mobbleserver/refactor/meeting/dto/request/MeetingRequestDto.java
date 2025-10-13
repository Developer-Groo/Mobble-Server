package com.mobble.mobbleserver.refactor.meeting.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.meeting.entity.Meeting;
import com.mobble.mobbleserver.refactor.meeting.entity.MeetingType;

import java.time.LocalDateTime;

public record MeetingRequestDto(
        String title,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime dateTime,
        String location,
        String cost,
        Integer memberLimit,
        MeetingType type
) {

    public Meeting toEntity(ClubMember hostMember) {
        return Meeting.createMeeting(
                hostMember,
                this.title,
                this.dateTime,
                this.location,
                this.cost,
                this.memberLimit,
                this.type
        );
    }
}
