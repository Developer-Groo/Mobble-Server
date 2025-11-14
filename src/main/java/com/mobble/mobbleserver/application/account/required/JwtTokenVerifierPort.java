package com.mobble.mobbleserver.application.account.required;

import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;

import java.util.List;
import java.util.Optional;

public interface JwtTokenVerifierPort {

    Optional<Long> extractMemberId(String jwtToken);

    List<ClubMemberRole> extractRoles(String jwtToken);

    boolean isValid(String jwtToken);
}
