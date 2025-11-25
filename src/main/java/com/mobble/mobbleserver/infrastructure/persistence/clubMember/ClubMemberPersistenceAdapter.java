package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberWritePort;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClubMemberPersistenceAdapter implements ClubMemberWritePort, ClubMemberReadPort {

    private final JpaClubMemberRepository repository;

    /* ClubMemberWritePort */
    @Override
    public ClubMember save(ClubMember clubMember) {
        return repository.save(clubMember);
    }

    @Override
    public void deleteAllByClubId(Long clubId) {
        repository.deleteAllByClubId(clubId);
    }

    /* ClubMemberReadPort */
    @Override
    public Optional<ClubMember> findClubMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return repository.findClubMemberByClubIdAndMemberId(clubId, memberId);
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
}
