package com.mobble.mobbleserver.application.member.port.provided;

import com.mobble.mobbleserver.application.member.command.UpdateMemberCommand;
import com.mobble.mobbleserver.domain.member.Member;

public interface MemberUpdatePort {

    Member updateMember(UpdateMemberCommand command);
}
