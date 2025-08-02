package com.mobble.mobbleserver.account.auth.principal;

import com.mobble.mobbleserver.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public record CustomUserDetails(Member member, boolean isNewUser, Map<String, Object> attributes) implements UserDetails, OAuth2User {

    public static CustomUserDetails existingMember(Member member, Map<String, Object> attributes) {
        return new CustomUserDetails(member, false, attributes);
    }

    public static CustomUserDetails newMember(String email, String name, String socialProvider, String socialId) {
        Map<String, Object> attributes = Map.of(
                "email", email,
                "name", name,
                "socialProvider", socialProvider,
                "socialId", socialId
        );

        return new CustomUserDetails(null, true, attributes);
    }

    @Override
    public String getName() {
        if (isNewUser) {
            return attributes.get("name").toString();
        }
        return member.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        if (isNewUser) {
            return attributes.get("email").toString();
        }
        return member.getEmail();
    }

    @Override
    public boolean isEnabled() {
        if (isNewUser) {
            return true;
        }
        return !member.isDeleted();
    }
}
