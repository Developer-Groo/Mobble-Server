package com.mobble.mobbleserver.application.clubMember.port.provided;

public interface ClubMemberLeavePort {

    void leave(Long memberId, Long clubId);
}
