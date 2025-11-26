package com.mobble.mobbleserver.infrastructure.web.member.dto.response;

import com.mobble.mobbleserver.domain.member.Gender;
import com.mobble.mobbleserver.domain.member.Member;

public record MemberResponseDto(
        Long memberId,
        String name,
        Gender gender,
        String email,
        String phone,
        String profileImageUrl
) {

    public static MemberResponseDto toDto(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getGender(),
                member.getEmail(),
                member.getPhone(),
                member.getProfileImage().getUrl()
        );
    }
}
