package com.mobble.mobbleserver.refactor.member.dto.response;

import com.mobble.mobbleserver.refactor.ground.dto.response.GroundResponseDto;
import com.mobble.mobbleserver.refactor.member.entity.Gender;
import com.mobble.mobbleserver.refactor.member.entity.Member;

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
