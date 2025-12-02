package com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response;

import com.mobble.mobbleserver.application.meetingMember.result.MeetingMemberInfoResult;

import java.util.List;

public record MeetingMemberListResponseDto(Long meetingId, List<MeetingMemberInfoResult> attendedMembers) {

    public static MeetingMemberListResponseDto toDto(Long meetingId, List<MeetingMemberInfoResult> attendedMembers) {
        return new MeetingMemberListResponseDto(meetingId, attendedMembers);
    }
}
