package com.mobble.mobbleserver.application.club.core.port.provided;

public interface ClubDeletePort {

    void deleteClub(Long clubId, Long memberId);
}
