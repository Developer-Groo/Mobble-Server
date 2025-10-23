package com.mobble.mobbleserver.infrastructure.persistence.clubMember;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;

import java.util.List;

public interface ClubMemberQueryDslRepository {

    List<ClubMember> findAllClubMemberByMemberId(Long memberId);
}
