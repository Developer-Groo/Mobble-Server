package com.mobble.mobbleserver.domain.member.dto.response;

import com.mobble.mobbleserver.domain.member.entity.Member;

public record MemberCreateResponseDto(Long id, String name, String email) {

    public static MemberCreateResponseDto toDto(Member member) {
        return new MemberCreateResponseDto(member.getId(), member.getName(), member.getEmail());
    }
}
