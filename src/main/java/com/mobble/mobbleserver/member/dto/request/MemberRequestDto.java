package com.mobble.mobbleserver.member.dto.request;

import com.mobble.mobbleserver.account.oauth2.provider.SocialProvider;
import com.mobble.mobbleserver.member.entity.Gender;
import com.mobble.mobbleserver.member.entity.Member;

public record MemberRequestDto(
        String name,
        int age,
        Gender gender,
        String email,
        String phone,
        String ground,
        String profileImage,
        boolean termsAgreed,
        boolean privacyAgreed,
        SocialProvider socialProvider,
        String socialId
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
                this.privacyAgreed,
                this.socialProvider,
                this.socialId
        );
    }
}
