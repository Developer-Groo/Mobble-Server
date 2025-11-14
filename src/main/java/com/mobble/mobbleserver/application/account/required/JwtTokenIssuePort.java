package com.mobble.mobbleserver.application.account.required;

import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;

import java.util.List;

public interface JwtTokenIssuePort {

    String createJwtToken(Long memberId, List<ClubMemberRole> roles);

    String createJwtToken(Long memberId);
}
