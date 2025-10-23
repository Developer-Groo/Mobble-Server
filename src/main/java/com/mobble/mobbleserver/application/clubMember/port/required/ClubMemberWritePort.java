package com.mobble.mobbleserver.application.clubMember.port.required;

import com.mobble.mobbleserver.domain.clubMember.ClubMember;

public interface ClubMemberWritePort {

    ClubMember save(ClubMember clubMember);

    void deleteAllClubMemberByClubId(Long clubId);
}
