package com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response;

import com.mobble.mobbleserver.application.meetingMember.result.MeetingMemberInfoResult;

import java.util.List;

public record MeetingMemberInfoListResponseDto(Long meetingId, List<MeetingMemberInfoResult> attendedMembers) {

    public static MeetingMemberInfoListResponseDto toDto(Long meetingId, List<MeetingMemberInfoResult> attendedMembers) {
        return new MeetingMemberInfoListResponseDto(meetingId, attendedMembers);
    }
}
