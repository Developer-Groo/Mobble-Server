package com.mobble.mobbleserver.application.club.clubGround.port.required;

import com.mobble.mobbleserver.domain.club.clubGround.ClubGround;
import com.mobble.mobbleserver.domain.ground.Ground;

import java.util.List;

public interface ClubGroundReadPort {

    List<ClubGround> findByClubId(Long clubId);

    List<Ground> findGroundsByClubId(Long clubId);
}
