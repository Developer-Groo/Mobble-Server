package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;

import java.util.Collection;
import java.util.List;

public interface ClubMemberQueryDslRepository {

    List<ClubMemberRole> findDistinctRolesByMemberIdAndRoleIn(Long memberId, Collection<ClubMemberRole> roles);
}
