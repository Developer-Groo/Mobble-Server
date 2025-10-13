package com.mobble.mobbleserver.refactor.clubMember.repository;

import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;

import java.util.List;

public interface ClubMemberQueryRepository {

    List<ClubMember> findAllClubMemberByMemberId(Long memberId);
}
