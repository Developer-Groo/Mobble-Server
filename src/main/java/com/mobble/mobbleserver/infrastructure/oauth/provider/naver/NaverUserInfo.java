package com.mobble.mobbleserver.infrastructure.oauth.provider.naver;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.error.OAuthBusinessError;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.infrastructure.oauth.common.OAuth2UserInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> response;

    public NaverUserInfo(Map<String, Object> rawResponse) {
        Object response = rawResponse.get("response");
        if (!(response instanceof Map<?,?> map)) throw new BusinessException(OAuthBusinessError.INVALID_USER_INFO);

        Map<String, Object> safe = new HashMap<>();

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String key)) throw new BusinessException(OAuthBusinessError.INVALID_USER_INFO);
            safe.put(key, entry.getValue());
        }

        this.response = Collections.unmodifiableMap(safe);
    }

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.NAVER;
    }

    @Override
    public String getProviderId() {
        if (response.get("id") == null) throw new BusinessException(OAuthBusinessError.INVALID_USER_INFO);

        return response.get("id").toString();
    }

    @Override
    public String getEmail() {
        if (response.get("email") == null) throw new BusinessException(OAuthBusinessError.INVALID_USER_INFO);

        return response.get("email").toString();
    }
}
