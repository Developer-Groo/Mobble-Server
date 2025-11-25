package com.mobble.mobbleserver.application.clubMember.service;

import com.mobble.mobbleserver.application.club.error.ClubBusinessError;
import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.clubMember.command.UpdateRoleCommand;
import com.mobble.mobbleserver.application.clubMember.command.UpdateStatusCommand;
import com.mobble.mobbleserver.application.clubMember.error.ClubMemberBusinessError;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberJoinPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberLeavePort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberUpdatePort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberWritePort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubMemberModifyService implements ClubMemberJoinPort, ClubMemberUpdatePort, ClubMemberLeavePort {

    private final ClubMemberWritePort clubMemberWritePort;

    private final ClubMemberReadPort clubMemberReadPort;
    private final MemberReadPort memberReadPort;
    private final ClubReadPort clubReadPort;

    @Override
    public ClubMember join(Long memberId, Long clubId) {
        Club club = assertClubByClubId(clubId);
        Member member = assertMemberByMemberId(memberId);

        Optional<ClubMember> optionalMember = clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .map(existingMember -> {
                    if (existingMember.isActive()) throw new IllegalStateException();
                    if (!existingMember.canRejoin()) throw new IllegalStateException();
                    return existingMember;
                });

        JoinStatus joinStatus = club.isAutoJoin()
                ? JoinStatus.APPROVED
                : JoinStatus.WAITING;

        ClubMember clubMember = optionalMember
                .map(existingMember -> existingMember.updateStatus(joinStatus))
                .orElseGet(() -> ClubMember.createMember(member, club, joinStatus));

        if (joinStatus == JoinStatus.APPROVED) {
            club.increaseMemberCount();
        }

        ClubMember saved = clubMemberWritePort.save(clubMember);

        // Todo: 알림 처리 (구현 예정)
        // handleJoinNotification(club, member, joinStatus);

        return saved;
    }

    @Override
    public ClubMember updateJoinStatus(UpdateStatusCommand command) {
        ClubMember leader = assertClubMemberByClubIdAndMemberId(command.clubId(), command.leaderId());
        assertLeader(leader);

        ClubMember targetMember = assertClubMemberByClubIdAndMemberId(command.clubId(), command.targetMemberId());
        JoinStatus current = targetMember.getJoinStatus();
        JoinStatus target = command.targetStatus();

        if (command.leaderId().equals(command.targetMemberId())) throw new IllegalStateException();

        if (!current.canTransitionTo(target)) throw new IllegalStateException();

        if (current == JoinStatus.WAITING && target == JoinStatus.APPROVED) {
            targetMember.getClub().increaseMemberCount();
        } else if (current == JoinStatus.APPROVED && target == JoinStatus.KICKED) {
            targetMember.getClub().decreaseMemberCount();
        }

        targetMember.updateStatus(target);

        // Todo: 알림 처리 (구현 예정)
        // sendStatusChangeNotification(clubMember, target);

        return targetMember;
    }

    @Override
    public ClubMember updateRole(UpdateRoleCommand command) {
        ClubMember leader = assertClubMemberByClubIdAndMemberId(command.clubId(), command.leaderId());
        assertLeader(leader);

        ClubMember targetMember = assertClubMemberByClubIdAndMemberId(command.clubId(), command.targetMemberId());

        if (command.leaderId().equals(command.targetMemberId())) throw new BusinessException(ClubMemberBusinessError.SELF_ROLE_CHANGE_NOT_ALLOWED);

        targetMember.assertApproved();

        ClubMemberRole currentRole = targetMember.getClubMemberRole();
        ClubMemberRole newRole = command.newRole();

        if (!currentRole.canChangeTo(newRole)) throw new IllegalStateException();

        targetMember.updateRole(command.newRole());

        // Todo: 알림 처리 (구현 예정)
        // sendRoleChangeNotification(clubMember, target);

        return targetMember;
    }

    @Override
    public void leave(Long memberId, Long clubId) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(clubId, memberId);

        if (clubMember.isLeader()) throw new IllegalStateException();

        clubMember.assertApproved();

        clubMember.getClub().decreaseMemberCount();
        clubMember.updateStatus(JoinStatus.WITHDRAWN);
    }

    /* ==== Private Helper ==== */
    private Club assertClubByClubId(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new BusinessException(ClubBusinessError.NOT_FOUND));
    }

    private Member assertMemberByMemberId(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER)); // Todo: Error 수정 필요
    }

    private ClubMember assertClubMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new BusinessException(ClubMemberBusinessError.NOT_JOINED_CLUB));
    }

    private void assertLeader(ClubMember clubMember) {
        if (!clubMember.isLeader()) throw new BusinessException(ClubMemberBusinessError.ONLY_LEADER_ALLOWED);
    }

    private void handleJoinNotification(Club club, Member member, JoinStatus joinStatus) {
        if (joinStatus.equals(JoinStatus.WAITING)) {
            // Todo: 관리자 승인 필요
            //  "가입 요청" 알림 전송 로직: Service port
        }

        if (joinStatus.equals(JoinStatus.APPROVED)) {
            // Todo: 자동 가입
            //  "새 멤버 가입" 알림 전송 로직: Service port
        }
    }
}
