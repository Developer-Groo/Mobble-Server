package com.mobble.mobbleserver.application.member.port.provided;

import com.mobble.mobbleserver.infrastructure.web.member.dto.request.MemberUpdateRequestDto;
import com.mobble.mobbleserver.infrastructure.web.member.dto.response.MemberResponseDto;

public interface MemberUpdatePort {

    MemberResponseDto updateMember(Long memberId, MemberUpdateRequestDto dto);
}
