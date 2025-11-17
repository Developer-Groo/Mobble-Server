package com.mobble.mobbleserver.domain.notification.device;

import com.mobble.mobbleserver.domain.common.BaseEntity;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false)
    private Platform platform;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Builder(access = AccessLevel.PRIVATE)
    private DeviceToken(Member member, String token, Platform platform, boolean enabled) {
        this.member = member;
        this.token = token;
        this.platform = platform;
        this.enabled = enabled;
    }

    public static DeviceToken create(Member member, String token, Platform platform) {
        return DeviceToken.builder()
                .member(member)
                .token(token)
                .platform(platform)
                .enabled(true)
                .build();
    }

    public void disable() {
        this.enabled = false;
    }

    public void reassignTo(Member newOwner) {
        this.member = newOwner;
        this.enabled = true;
    }
}
