package com.mobble.mobbleserver.domain.clubMember.service;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.club.validator.ClubValidator;
import com.mobble.mobbleserver.domain.clubMember.dto.request.UpdateClubMemberRoleDto;
import com.mobble.mobbleserver.domain.clubMember.dto.request.UpdateClubMemberStatusDto;
import com.mobble.mobbleserver.domain.clubMember.dto.response.ClubMemberResponseDto;
import com.mobble.mobbleserver.domain.clubMember.dto.response.ClubMemberUpsertResponseDto;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.domain.clubMember.repository.ClubMemberRepository;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;

    private final ClubValidator clubValidator;
    private final MemberValidator memberValidator;
    private final ClubMemberValidator clubMemberValidator;

    @Transactional
    public ClubMemberUpsertResponseDto joinClub(Long memberId, Long clubId) {
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        boolean existsClubMember = existsClubMember(clubId, memberId);
        if (existsClubMember) throw new DomainException(ClubMemberErrorCode.ALREADY_JOINED);

        validateClubNotFull(club);

        JoinStatus joinStatus = determineJoinStatus(club);

        ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.MEMBER, joinStatus);
        clubMemberRepository.save(clubMember);

        return ClubMemberUpsertResponseDto.toDto(clubMember);
    }

    @Transactional
    public void withdrawClub(Long memberId, Long clubId) {
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        boolean existsClubMember = existsClubMember(clubId, memberId);
        if (!existsClubMember) throw new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB);

        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        clubMember.updateStatus(JoinStatus.WITHDRAWN);
    }

    @Transactional
    public ClubMemberUpsertResponseDto updateClubMemberRole(Long clubId, Long loginedMemberId, UpdateClubMemberRoleDto dto) {
        Long targetMemberId = dto.memberId();
        ClubMemberRole newRole = dto.newRole();

        clubValidator.findClubByClubIdOrThrow(clubId);
        memberValidator.findMemberByMemberIdOrThrow(targetMemberId);
        memberValidator.findMemberByMemberIdOrThrow(loginedMemberId);

        validateLeader(clubId, loginedMemberId);

        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, targetMemberId);

        if (targetMemberId.equals(loginedMemberId)) throw new DomainException(ClubMemberErrorCode.CANNOT_CHANGE_OWN_ROLE);

        clubMember.updateRole(newRole);

        return ClubMemberUpsertResponseDto.toDto(clubMember);
    }

    @Transactional
    public ClubMemberUpsertResponseDto updateClubMemberStatus(Long clubId, Long loginedMemberId, UpdateClubMemberStatusDto dto) {
        Long memberId = dto.memberId();
        JoinStatus targetStatus = dto.status();

        clubValidator.findClubByClubIdOrThrow(clubId);
        memberValidator.findMemberByMemberIdOrThrow(memberId);

        validateLeader(clubId, loginedMemberId);
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        if (targetStatus == JoinStatus.APPROVED) {
            validateClubNotFull(clubMember.getClub());
        }

        clubMember.updateStatus(targetStatus);

        return ClubMemberUpsertResponseDto.toDto(clubMember);
    }

    public List<ClubMemberResponseDto> findClubMembers(Long clubId) {
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);

        List<ClubMember> clubMembers = clubMemberRepository.findByClubId(clubId);

        return clubMembers.stream()
                .map(ClubMemberResponseDto::toEntity)
                .collect(Collectors.toList());
    }

    private void validateLeader(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        if (!clubMember.getClubMemberRole().equals(ClubMemberRole.LEADER)) {
            throw new DomainException(ClubErrorCode.NO_PERMISSION);
        }
    }

    private JoinStatus determineJoinStatus(Club club) {
        return club.isAutoJoin() ? JoinStatus.APPROVED : JoinStatus.WAITING;
    }

    private boolean existsClubMember(Long clubId, Long memberId) {
        return clubMemberRepository.findClubMemberByClubIdAndMemberId(clubId, memberId).isPresent();
    }

    public void validateClubNotFull(Club club) {
        long approvedCount = clubMemberRepository.countByClubIdAndJoinStatus(club.getId(), JoinStatus.APPROVED);
        if (approvedCount >= club.getHeadCount()) {
            throw new DomainException(ClubMemberErrorCode.CLUB_IS_FULL);
        }
    }
}
