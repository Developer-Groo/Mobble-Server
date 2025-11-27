package com.mobble.mobbleserver.application.clubMember.result;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;

import java.util.ArrayList;
import java.util.List;

public record ClubMembersResult(
        Long clubId,
        List<ClubMemberSummaryResult> approvedMembers,
        List<ClubMemberSummaryResult> waitingMembers,
        List<ClubMemberSummaryResult> leaveMembers,
        List<ClubMemberSummaryResult> kickedMembers,
        List<ClubMemberSummaryResult> rejectedMembers
) {

    public static ClubMembersResult create(Long clubId, List<ClubMember> clubMembers) {
        List<ClubMemberSummaryResult> approved = new ArrayList<>();
        List<ClubMemberSummaryResult> waiting = new ArrayList<>();
        List<ClubMemberSummaryResult> leave = new ArrayList<>();
        List<ClubMemberSummaryResult> kicked = new ArrayList<>();
        List<ClubMemberSummaryResult> rejected = new ArrayList<>();

        for (ClubMember clubMember : clubMembers) {
            ClubMemberSummaryResult summary = ClubMemberSummaryResult.create(clubMember);
            switch (clubMember.getJoinStatus()) {
                case APPROVED -> approved.add(summary);
                case WAITING -> waiting.add(summary);
                case LEAVE -> leave.add(summary);
                case KICKED -> kicked.add(summary);
                case REJECTED -> rejected.add(summary);
            }
        }

        return new ClubMembersResult(
                clubId,
                approved,
                waiting,
                leave,
                kicked,
                rejected
        );
    }
}
