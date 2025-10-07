package com.mobble.mobbleserver.domain.member.dto.response;

import com.mobble.mobbleserver.domain.ground.dto.response.GroundResponseDto;
import com.mobble.mobbleserver.domain.member.entity.Gender;
import com.mobble.mobbleserver.domain.member.entity.Member;

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
                GroundResponseDto.toDto(member.getGround()),
                member.getProfileImage()
        );
    }
}
