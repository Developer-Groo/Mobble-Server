package com.mobble.mobbleserver.domain.member.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "email", unique = true)
    private String email;

    // Todo 소셜 로그인 구현 후 삭제
    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "ground")
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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(
            String name,
            int age,
            Gender gender,
            String email,
            String password, // Todo 소셜 로그인 구현 후 삭제
            String phone,
            String ground,
            String profileImage,
            boolean termsAgreed,
            boolean privacyAgreed,
            boolean isDeleted
//            SocialProvider socialProvider,
//            String socialId,
    ) {
        validateCommon(name, gender, phone, ground, termsAgreed, privacyAgreed);
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.password = password; // Todo 소셜 로그인 구현 후 삭제
        this.phone = phone;
        this.ground = ground;
        this.profileImage = profileImage;
        this.termsAgreed = termsAgreed;
        this.privacyAgreed = privacyAgreed;
        this.isDeleted = isDeleted;
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
            String password, // Todo 소셜 로그인 구현 후 삭제
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
                .password(password) // Todo 소셜 로그인 구현 후 삭제
                .phone(phone)
                .ground(ground)
                .profileImage(profileImage)
                .termsAgreed(termsAgreed)
                .privacyAgreed(privacyAgreed)
                .isDeleted(false)
//                .socialProvider(socialProvider)
//                .socialId(socialId)
                .build();
    }

    public Member updateMember(String ground, String profileImage) {
        if (ground == null || ground.isBlank()) throw new DomainException(MemberErrorCode.GROUND_REQUIRED);
        this.ground = ground;
        this.profileImage = profileImage;
        return this;
    }

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    private void validateCommon(
            String name,
            Gender gender,
            String phone,
            String ground,
            boolean termsAgreed,
            boolean privacyAgreed
    ) {
        if (name == null) throw new DomainException(MemberErrorCode.NAME_REQUIRED);
        if (gender == null) throw new DomainException(MemberErrorCode.GENDER_REQUIRED);
        if (phone == null) throw new DomainException(MemberErrorCode.PHONE_REQUIRED);
        if (ground == null) throw new DomainException(MemberErrorCode.GROUND_REQUIRED);
        if (!termsAgreed) throw new DomainException(MemberErrorCode.TERMS_AGREED_REQUIRED);
        if (!privacyAgreed) throw new DomainException(MemberErrorCode.PRIVACY_AGREED_REQUIRED);
    }
}
