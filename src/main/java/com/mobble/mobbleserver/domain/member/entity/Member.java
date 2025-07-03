package com.mobble.mobbleserver.domain.member.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

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

    @Column(name = "social_provider")
    private String socialProvider;

    @Column(name = "sicial_id")
    private String socialId;

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
    }

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
                .build();
    }

    // 임시
    public static Member withId(Long id) {
        Member member = new Member();
        Field idField = ReflectionUtils.findField(Member.class, "id");
        if (idField != null) {
            idField.setAccessible(true);
            ReflectionUtils.setField(idField, member, id);
        }
        return member;
    }
}
