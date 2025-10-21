package com.mobble.mobbleserver.infrastructure.web.meetingMember.dto.response;

import com.mobble.mobbleserver.domain.meetingMember.MeetingMember;

import java.util.List;

public record MeetingMemberListResponseDto(Long meetingId, List<MeetingMemberResponseDto> meetingMembers) {

    public static MeetingMemberListResponseDto toDto(List<MeetingMember> meetingMemberList) {
        Long meetingId = meetingMemberList.get(0).getMeeting().getId();

        List<MeetingMemberResponseDto> meetingMembers = meetingMemberList.stream()
                .map(meetingMember -> MeetingMemberResponseDto.toDto(meetingMember.getMember()))
                .toList();

        return new MeetingMemberListResponseDto(meetingId, meetingMembers);
    }
}
