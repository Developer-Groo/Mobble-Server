package com.mobble.mobbleserver.application.club.port.provided;

import com.mobble.mobbleserver.application.club.result.ClubResult;

public interface ClubQueryPort {

    ClubResult getClub(Long clubId, Long memberId);
}
