package com.mobble.mobbleserver.infrastructure.web.member.dto.response;

import com.mobble.mobbleserver.domain.member.Member;

public record MemberCreateResponseDto(Long id, String name, String email) {

    public static MemberCreateResponseDto toDto(Member member) {
        return new MemberCreateResponseDto(member.getId(), member.getName(), member.getEmail());
    }
}
