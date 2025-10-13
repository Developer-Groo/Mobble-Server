package com.mobble.mobbleserver.refactor.meetingMember.dto.response;

import com.mobble.mobbleserver.refactor.meetingMember.entity.MeetingMember;

import java.util.List;

public record MeetingMemberListResponseDto(Long meetingId, List<MeetingMemberResponseDto> meetingMembers) {

    public static MeetingMemberListResponseDto toDto(Long meetingId, List<MeetingMember> meetingMemberList) {
        List<MeetingMemberResponseDto> meetingMembers = meetingMemberList.stream()
                .map(meetingMember -> MeetingMemberResponseDto.toDto(meetingMember.getMember()))
                .toList();

        return new MeetingMemberListResponseDto(meetingId, meetingMembers);
    }
}
