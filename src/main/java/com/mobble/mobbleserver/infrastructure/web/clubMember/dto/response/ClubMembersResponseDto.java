package com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response;

import com.mobble.mobbleserver.application.clubMember.result.ClubMembersResult;

import java.util.List;

public record ClubMembersResponseDto(
        Long clubId,
        List<ClubMemberSummaryDto> approvedMembers,
        List<ClubMemberSummaryDto> waitingMembers,
        List<ClubMemberSummaryDto> leaveMembers,
        List<ClubMemberSummaryDto> kickedMembers,
        List<ClubMemberSummaryDto> rejectedMembers
) {

    public static ClubMembersResponseDto toDto(ClubMembersResult result) {
        return new ClubMembersResponseDto(
                result.clubId(),
                result.approvedMembers().stream()
                        .map(ClubMemberSummaryDto::toDto)
                        .toList(),
                result.waitingMembers().stream()
                        .map(ClubMemberSummaryDto::toDto)
                        .toList(),
                result.leaveMembers().stream()
                        .map(ClubMemberSummaryDto::toDto)
                        .toList(),
                result.kickedMembers().stream()
                        .map(ClubMemberSummaryDto::toDto)
                        .toList(),
                result.rejectedMembers().stream()
                        .map(ClubMemberSummaryDto::toDto)
                        .toList()
        );
    }
}
