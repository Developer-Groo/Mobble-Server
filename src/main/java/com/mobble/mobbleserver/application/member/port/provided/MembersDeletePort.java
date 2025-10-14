package com.mobble.mobbleserver.application.member.port.provided;

import java.time.LocalDateTime;

public interface MembersDeletePort {

    void deleteMembers(LocalDateTime softDeletedDate);
}
