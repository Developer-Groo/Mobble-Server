package com.mobble.mobbleserver.account.auth.dto.response;

import com.mobble.mobbleserver.domain.member.entity.Member;

public record LoginResponseDto(Long id) {

    public static LoginResponseDto toDto(Member member) {
        return new LoginResponseDto(member.getId());
    }

}
