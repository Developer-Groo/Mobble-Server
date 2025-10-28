package com.mobble.mobbleserver.application.club.clubGround.port.required;

import com.mobble.mobbleserver.domain.club.clubGround.ClubGround;

import java.util.List;

public interface ClubGroundWritePort {

    void saveAll(List<ClubGround> clubGrounds);

    void deleteAllByClubId(Long clubId);
}
