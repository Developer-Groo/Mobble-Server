package com.mobble.mobbleserver.account.user;

import com.mobble.mobbleserver.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public record CustomUserDetails(Member member, boolean isNewUser, Map<String, Object> attributes) implements UserDetails, OAuth2User {

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
        return null;
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
