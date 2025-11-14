package com.mobble.mobbleserver.application.account.required;

import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;

import java.util.List;
import java.util.Optional;

public interface JwtTokenParsePort {

    Optional<Long> getMemberIdByJwtToken(String jwtToken);

    List<ClubMemberRole> getRolesByJwtToken(String jwtToken);
}
