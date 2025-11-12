package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberWritePort;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.JoinStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClubMemberPersistenceAdapter implements ClubMemberWritePort, ClubMemberReadPort {

    private final JpaClubMemberRepository repository;

    @Override
    public ClubMember save(ClubMember clubMember) {
        return repository.save(clubMember);
    }

    @Override
    public void deleteAllClubMemberByClubId(Long clubId) {
        repository.deleteAllClubMemberByClubId(clubId);
    }

    @Override
    public Optional<ClubMember> findClubMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return repository.findClubMemberByClubIdAndMemberId(clubId, memberId);
    }

    @Override
    public long countByClubIdAndJoinStatus(Long clubId, JoinStatus joinStatus) {
        return repository.countByClubIdAndJoinStatus(clubId, joinStatus);
    }

    @Override
    public List<ClubMemberRole> findDistinctRolesByMemberIdAndRoleIn(Long memberId, List<ClubMemberRole> leader) {
        return repository.findDistinctRolesByMemberIdAndRoleIn(memberId, leader);
    }

    @Override
    public List<ClubMember> findByClubId(Long clubId) {
        return repository.findByClubId(clubId);
    }

    @Override
    public List<ClubMember> findAllClubMemberByMemberId(Long memberId) {
        return repository.findAllClubMemberByMemberId(memberId);
    }

    @Override
    public Optional<ClubMember> findByClubIdAndClubMemberRole(Long clubId, ClubMemberRole clubMemberRole) {
        return repository.findByClubIdAndClubMemberRole(clubId, clubMemberRole);
    }
}
