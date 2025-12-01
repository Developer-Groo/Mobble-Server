package com.mobble.mobbleserver.application.member.port.provided;

import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.web.member.dto.request.UpdateMemberRequestDto;

public interface MemberUpdatePort {

    Member updateMember(Long memberId, UpdateMemberRequestDto dto);
}
