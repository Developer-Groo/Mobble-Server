package com.mobble.mobbleserver.application.member.port.provided;

import java.time.LocalDateTime;

public interface DeleteWithdrawnMembersPort {

    void deleteWithdrawnMembers(LocalDateTime withdrewDate);
}
