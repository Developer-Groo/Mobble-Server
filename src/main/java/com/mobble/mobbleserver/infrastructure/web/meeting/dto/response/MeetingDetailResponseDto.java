package com.mobble.mobbleserver.infrastructure.web.meeting.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.application.meeting.result.MeetingResult;
import com.mobble.mobbleserver.application.meetingMember.result.MeetingMemberInfoResult;
import com.mobble.mobbleserver.domain.meeting.MeetingType;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingDetailResponseDto(
        Long meetingId,
        Long clubId,
        MeetingMemberInfoResult ownerInfo,
        String title,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime schedule,
        String location,
        String cost,
        Integer memberLimit,
        int attendeeCount,
        MeetingType type,
        int dDay,
        List<MeetingMemberInfoResult> attendedMembers,
        boolean isAttended
) {

    public static List<MeetingDetailResponseDto> create(List<MeetingResult> results) {
        return results.stream()
                .map(MeetingDetailResponseDto::toDto)
                .toList();
    }

    public static MeetingDetailResponseDto toDto(MeetingResult result) {
        return new MeetingDetailResponseDto(
                result.meetingId(),
                result.clubId(),
                result.ownerInfo(),
                result.title(),
                result.schedule(),
                result.location(),
                result.cost(),
                result.memberLimit(),
                result.attendeeCount(),
                result.type(),
                result.dDay(),
                result.attendedMembers(),
                result.isAttended()
        );
    }
}
