package com.mobble.mobbleserver.application.member.port.provided;

import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.web.member.dto.request.MemberUpdateRequestDto;

public interface MemberUpdatePort {

    Member updateMember(Long memberId, MemberUpdateRequestDto dto);
}
