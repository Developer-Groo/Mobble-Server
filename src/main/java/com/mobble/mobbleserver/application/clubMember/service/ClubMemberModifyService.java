package com.mobble.mobbleserver.application.clubMember.service;

import com.mobble.mobbleserver.application.account.required.JwtTokenIssuerPort;
import com.mobble.mobbleserver.application.club.error.ClubBusinessError;
import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberJoinPort;
import com.mobble.mobbleserver.application.clubMember.port.provided.ClubMemberDeletePort;
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
public class ClubMemberModifyService implements ClubMemberJoinPort, ClubMemberUpdatePort, ClubMemberDeletePort {

    private final JwtTokenIssuerPort jwtTokenIssuerPort;

    private final ClubMemberWritePort clubMemberWritePort;

    private final ClubMemberReadPort clubMemberReadPort;
    private final MemberReadPort memberReadPort;
    private final ClubReadPort clubReadPort;

    @Override
    public ClubMember join(Long memberId, Long clubId) {
        assertNotJoined(clubId, memberId);
        
        Club club = assertClubByClubId(clubId);
        Member member = assertMemberByMemberId(memberId);

        JoinStatus joinStatus = determineInitialJoinStatus(club);

        ClubMember clubMember = ClubMember.createMember(member, club, joinStatus);
        ClubMember saved = clubMemberWritePort.save(clubMember);

        if (joinStatus.equals(JoinStatus.WAITING)) {
            // Todo: 관리자 승인 필요
            //  "가입 요청" 알림 전송 로직: Service port
        } else if (joinStatus.equals(JoinStatus.APPROVED)) {
            // Todo: 자동 가입
            //  "새 멤버 가입" 알림 전송 로직: Service port
        }

        return saved;
    }

    @Override
    public ClubMemberUpsertResponseDto updateClubMemberJoinStatus(Long clubId, Long loginedMemberId, UpdateClubMemberStatusDto dto) {
        Long targetMemberId = dto.memberId();
        JoinStatus targetStatus = dto.status();

        Club club = assertClubByClubId(clubId);
        Member member = assertMemberByMemberId(targetMemberId);
        Member loginedMember = assertMemberByMemberId(loginedMemberId);
        ClubMember clubLeader = findClubMemberByClubIdAndMemberIdOrThrow(clubId, loginedMember.getId());
        assertLeader(clubLeader);

        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, member.getId());

        if (targetStatus == JoinStatus.APPROVED) {
//            validateClubNotFull(club);
        }

        clubMember.updateStatus(targetStatus);

        return ClubMemberUpsertResponseDto.toDto(clubMember);
    }

    @Override
    public ClubMemberRoleUpdateResultDto updateClubMemberRole(Long clubId, Long loginedMemberId, UpdateClubMemberRoleDto dto) {
        Long targetMemberId = dto.memberId();
        ClubMemberRole newRole = dto.newRole();

        Club club = assertClubByClubId(clubId);
        Member member = assertMemberByMemberId(targetMemberId);
        Member loginedMember = assertMemberByMemberId(loginedMemberId);
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

    @Override
    public void leaveClub(Long memberId, Long clubId) {
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Club club = clubMember.getClub();
        Member member = clubMember.getMember();

        clubMember.updateStatus(JoinStatus.WITHDRAWN);
        // Todo: 클럽 탈퇴 시 역할(ClubMemberRole) 처리 방식 확정 후 반영
        //  - 현재는 역할 유지 상태
        //  - 추후 권한 초기화 or tokenVersion 증가 필요 여부 판단
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

    private ClubMember findClubMemberByClubIdAndMemberIdOrThrow(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB)); // Todo: Error 수정 필요
    }

    private void assertNotJoined(Long clubId, Long memberId) {
        boolean exists = clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .isPresent();

        if (exists) throw new DomainException(ClubMemberErrorCode.ALREADY_JOINED); // Todo: Error 수정 필요
    }

    private JoinStatus determineInitialJoinStatus(Club club) {
        return club.isAutoJoin() ? JoinStatus.APPROVED : JoinStatus.WAITING;
    }

    private void assertLeader(ClubMember clubMember) {
        if (!clubMember.isLeader()) throw new DomainException(ClubMemberErrorCode.NO_PERMISSION); // Todo: Error 수정 필요
    }
}
