package com.mobble.mobbleserver.domain.member;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.domain.common.BaseEntity;
import com.mobble.mobbleserver.domain.common.Location;
import com.mobble.mobbleserver.domain.image.Image;
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

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "age")
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Embedded
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;

    @Column(name = "terms_agreed")
    private boolean termsAgreed;

    @Column(name = "privacy_agreed")
    private boolean privacyAgreed;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_provider", nullable = false)
    private SocialProvider socialProvider;

    @Column(name = "social_id", nullable = false)
    private String socialId;

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
            String phone,
            Location location,
            Image profileImage,
            boolean termsAgreed,
            boolean privacyAgreed,
            boolean isDeleted,
            SocialProvider socialProvider,
            String socialId
    ) {
        validateCommon(name, gender, phone, termsAgreed, privacyAgreed);
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.profileImage = profileImage;
        this.termsAgreed = termsAgreed;
        this.privacyAgreed = privacyAgreed;
        this.isDeleted = isDeleted;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
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
            Location location,
            Image profileImage,
            boolean termsAgreed,
            boolean privacyAgreed,
            SocialProvider socialProvider,
            String socialId
    ) {

        return Member.builder()
                .name(name)
                .age(age)
                .gender(gender)
                .email(email)
                .phone(phone)
                .location(location)
                .profileImage(profileImage)
                .termsAgreed(termsAgreed)
                .privacyAgreed(privacyAgreed)
                .isDeleted(false)
                .socialProvider(socialProvider)
                .socialId(socialId)
                .build();
    }

    public Member updateMember(Location location, Image profileImage) {
        this.location = location;
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
            boolean termsAgreed,
            boolean privacyAgreed
    ) {
        if (name == null) throw new DomainException(MemberErrorCode.NAME_REQUIRED);
        if (gender == null) throw new DomainException(MemberErrorCode.GENDER_REQUIRED);
        if (phone == null) throw new DomainException(MemberErrorCode.PHONE_REQUIRED);
        if (!termsAgreed) throw new DomainException(MemberErrorCode.TERMS_AGREED_REQUIRED);
        if (!privacyAgreed) throw new DomainException(MemberErrorCode.PRIVACY_AGREED_REQUIRED);
    }
}
