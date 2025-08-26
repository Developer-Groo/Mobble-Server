package com.mobble.mobbleserver.domain.clubMember.repository;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;

import java.util.List;

public interface ClubMemberQueryRepository {

    List<ClubMember> findAllClubMemberByMemberId(Long memberId);
}
