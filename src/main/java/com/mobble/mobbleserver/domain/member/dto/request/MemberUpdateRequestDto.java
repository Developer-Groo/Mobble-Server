package com.mobble.mobbleserver.domain.member.dto.request;

public record MemberUpdateRequestDto(
        String name,
        String phone,
        String ground,
        String profileImage
) {
}
