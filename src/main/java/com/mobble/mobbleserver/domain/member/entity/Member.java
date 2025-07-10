package com.mobble.mobbleserver.domain.member.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String email;

    private String phone;

    private String ground;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "terms_agreed")
    private boolean termsAgreed;

    @Column(name = "privacy_agreed")
    private boolean privacyAgreed;

//    @Column(name = "social_provider")
//    private String socialProvider;
//
//    @Column(name = "sicial_id")
//    private String socialId;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(
            String name,
            int age,
            Gender gender,
            String email,
            String phone,
            String ground,
            String profileImage,
            boolean termsAgreed,
            boolean privacyAgreed
//            SocialProvider socialProvider,
//            String socialId,
    ) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.ground = ground;
        this.profileImage = profileImage;
        this.termsAgreed = termsAgreed;
        this.privacyAgreed = privacyAgreed;
//        this.socialProvider = socialProvider;
//        this.socialId = socialId;
    }

    /**
     * 소셜 회원가입 완료 시 회원 생성
     */
    public static Member createMember(
            String name,
            int age,
            Gender gender,
            String email,
            String phone,
            String ground,
            String profileImage,
            boolean termsAgreed,
            boolean privacyAgreed
//            SocialProvider socialProvider,
//            String socialId
    ) {

        return Member.builder()
                .name(name)
                .age(age)
                .gender(gender)
                .email(email)
                .phone(phone)
                .ground(ground)
                .profileImage(profileImage)
                .termsAgreed(termsAgreed)
                .privacyAgreed(privacyAgreed)
//                .socialProvider(socialProvider)
//                .socialId(socialId)
                .build();
    }

    public Member updateMember(String ground, String profileImage) {
        this.ground = ground;
        this.profileImage = profileImage;
        return this;
    }
}
