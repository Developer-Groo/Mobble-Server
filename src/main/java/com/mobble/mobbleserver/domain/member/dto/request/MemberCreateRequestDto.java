package com.mobble.mobbleserver.domain.member.dto.request;

import com.mobble.mobbleserver.domain.member.entity.Gender;
import com.mobble.mobbleserver.domain.member.entity.Member;

public record MemberCreateRequestDto(
        String name,
        int age,
        Gender gender,
        String email,
        String phone,
        String ground,
        String profileImage,
        boolean termsAgreed,
        boolean privacyAgreed
//        SocialProvider socialProvider,
//        String socialId
) {
    public Member toEntity() {
        return Member.createMember(
                this.name,
                this.age,
                this.gender,
                this.email,
                this.phone,
                this.ground,
                this.profileImage,
                this.termsAgreed,
                this.privacyAgreed
//                this.socialProvider,
//                this.socialId
        );
    }
}
