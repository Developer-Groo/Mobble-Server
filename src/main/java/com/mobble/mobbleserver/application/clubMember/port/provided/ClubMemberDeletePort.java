package com.mobble.mobbleserver.application.clubMember.port.provided;

public interface ClubMemberDeletePort {

    void withdrawClub(Long memberId, Long clubId);
}
