package com.mobble.mobbleserver.application.account.required;

import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;

import java.util.List;

public interface JwtTokenIssuerPort {

    String issueJwtToken(Long memberId, List<ClubMemberRole> roles);

    String issueJwtToken(Long memberId);
}
