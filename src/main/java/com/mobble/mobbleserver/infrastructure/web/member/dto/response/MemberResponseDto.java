package com.mobble.mobbleserver.infrastructure.web.member.dto.response;

import com.mobble.mobbleserver.domain.member.Gender;
import com.mobble.mobbleserver.domain.member.Member;

public record MemberResponseDto(
        Long memberId,
        String name,
        Gender gender,
        String email,
        String phone,

        String address1,
        String address2,
        String city,
        String district,
        Double latitude,
        Double longitude,

        Long profileImageId,
        String profileImageUrl
) {

    public static MemberResponseDto toDto(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getGender(),
                member.getEmail(),
                member.getPhone(),

                member.getLocation().getAddress1(),
                member.getLocation().getAddress2(),
                member.getLocation().getCity(),
                member.getLocation().getDistrict(),
                member.getLocation().getLatitude(),
                member.getLocation().getLongitude(),

                member.getProfileImage() != null ? member.getProfileImage().getId() : null,
                member.getProfileImage() != null ? member.getProfileImage().getUrl() : null
        );
    }
}
