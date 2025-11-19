package com.mobble.mobbleserver.application.clubMember.service;

import com.mobble.mobbleserver.application.account.required.JwtTokenIssuerPort;
import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberCreatePort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberDeletePort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberUpdatePort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request.UpdateClubMemberRoleDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.request.UpdateClubMemberStatusDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberRoleUpdateResultDto;
import com.mobble.mobbleserver.infrastructure.web.clubMember.dto.response.ClubMemberUpsertResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubMemberModifyService implements ClubMemberCreatePort, ClubMemberUpdatePort, ClubMemberDeletePort {

    private final ClubMemberWritePort clubMemberWritePort;

    private final ClubMemberReadPort clubMemberReadPort;
    private final MemberReadPort memberReadPort;
    private final ClubReadPort clubReadPort;

    private final JwtTokenIssuerPort jwtTokenIssuerPort;

    @Override
    public ClubMemberUpsertResponseDto joinClub(Long memberId, Long clubId) {
        Club club = findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        boolean existsClubMember = existsClubMember(clubId, memberId);
        if (existsClubMember) throw new DomainException(ClubMemberErrorCode.ALREADY_JOINED);

        validateClubNotFull(club);

        JoinStatus joinStatus = determineJoinStatus(club);

        ClubMember clubMember = ClubMember.createMember(member, club, joinStatus);
        clubMemberWritePort.save(clubMember);

        return ClubMemberUpsertResponseDto.toDto(clubMember);
    }

    @Override
    public void leaveClub(Long memberId, Long clubId) {
        Club club = findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        boolean existsClubMember = existsClubMember(clubId, memberId);
        if (!existsClubMember) throw new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB);

        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        clubMember.updateStatus(JoinStatus.WITHDRAWN);
        // Todo: 클럽 탈퇴 시 역할(ClubMemberRole) 처리 방식 확정 후 반영
        // - 현재는 역할 유지 상태
        // - 추후 권한 초기화 or tokenVersion 증가 필요 여부 판단
    }

    @Override
    public ClubMemberUpsertResponseDto updateClubMemberJoinStatus(Long clubId, Long loginedMemberId, UpdateClubMemberStatusDto dto) {
        Long targetMemberId = dto.memberId();
        JoinStatus targetStatus = dto.status();

        Club club = findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(targetMemberId);
        Member loginedMember = findMemberByMemberIdOrThrow(loginedMemberId);
        ClubMember clubLeader = findClubMemberByClubIdAndMemberIdOrThrow(clubId, loginedMember.getId());
        assertLeader(clubLeader);

        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, member.getId());

        if (targetStatus == JoinStatus.APPROVED) {
            validateClubNotFull(club);
        }

        clubMember.updateStatus(targetStatus);

        return ClubMemberUpsertResponseDto.toDto(clubMember);
    }

    @Override
    public ClubMemberRoleUpdateResultDto updateClubMemberRole(Long clubId, Long loginedMemberId, UpdateClubMemberRoleDto dto) {
        Long targetMemberId = dto.memberId();
        ClubMemberRole newRole = dto.newRole();

        Club club = findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(targetMemberId);
        Member loginedMember = findMemberByMemberIdOrThrow(loginedMemberId);
        ClubMember clubLeader = findClubMemberByClubIdAndMemberIdOrThrow(clubId, loginedMember.getId());
        assertLeader(clubLeader);

        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, targetMemberId);

        if (targetMemberId.equals(loginedMemberId))
            throw new DomainException(ClubMemberErrorCode.CANNOT_CHANGE_OWN_ROLE);

        clubMember.updateRole(newRole);

        // 권한 변경으로 Access Token 재발급
        List<ClubMemberRole> roles = clubMemberReadPort.findDistinctRolesByMemberIdAndRoleIn(member.getId(), List.of(ClubMemberRole.LEADER, ClubMemberRole.MANAGER));
        String jwtToken = jwtTokenIssuerPort.issueJwtToken(member.getId(), roles);

        return ClubMemberRoleUpdateResultDto.toDto(clubMember, jwtToken);
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    private ClubMember findClubMemberByClubIdAndMemberIdOrThrow(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }

    private boolean existsClubMember(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId).isPresent();
    }

    public void validateClubNotFull(Club club) {
        long approvedCount = clubMemberReadPort.countByClubIdAndJoinStatus(club.getId(), JoinStatus.APPROVED);
        if (approvedCount >= club.getMemberCount()) {
            throw new DomainException(ClubMemberErrorCode.CLUB_IS_FULL);
        }
    }

    private JoinStatus determineJoinStatus(Club club) {
        return club.isAutoJoin() ? JoinStatus.APPROVED : JoinStatus.WAITING;
    }

    private void assertLeader(ClubMember clubMember) {
        if (!clubMember.isLeader()) throw new DomainException(ClubMemberErrorCode.NO_PERMISSION);
    }
}
