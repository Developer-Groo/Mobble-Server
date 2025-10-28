package com.mobble.mobbleserver.infrastructure.web.member.dto.response;

import com.mobble.mobbleserver.domain.member.Gender;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.web.ground.dto.response.GroundResponseDto;

public record MemberResponseDto(
        Long memberId,
        String name,
        Gender gender,
        String email,
        String phone,
        GroundResponseDto ground,
        String profileImage
) {

    public static MemberResponseDto toDto(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getGender(),
                member.getEmail(),
                member.getPhone(),
//                GroundResponseDto.toDto(member.getGround()),
                null,
                member.getProfileImage()
        );
    }
}
