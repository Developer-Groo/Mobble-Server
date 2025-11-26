package com.mobble.mobbleserver.application.clubMember.port.provided;

import com.mobble.mobbleserver.application.clubMember.command.UpdateRoleCommand;
import com.mobble.mobbleserver.application.clubMember.command.UpdateStatusCommand;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;

public interface ClubMemberUpdatePort {

    ClubMember updateJoinStatus(UpdateStatusCommand command);

    ClubMember updateRole(UpdateRoleCommand command);
}
