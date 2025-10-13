package com.mobble.mobbleserver.application.member.port.provided;

import com.mobble.mobbleserver.infrastructure.web.member.dto.response.MemberResponseDto;

public interface MemberQueryPort {

    MemberResponseDto getMember(Long memberId);
}
