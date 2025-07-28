package com.mobble.mobbleserver.account.oauth2.service;

import com.mobble.mobbleserver.account.oauth2.dto.response.GoogleUserInfoResponse;
import com.mobble.mobbleserver.account.oauth2.dto.response.KakaoUserInfoResponse;
import com.mobble.mobbleserver.account.oauth2.dto.response.NaverUserInfoResponse;
import com.mobble.mobbleserver.account.oauth2.dto.response.OAuth2UserInfo;

import java.util.Map;

public enum SocialProvider {
    KAKAO {
        @Override
        public OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes) {
            return new KakaoUserInfoResponse(attributes);
        }
    },

    NAVER {
        @Override
        public OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes) {
            return new NaverUserInfoResponse(attributes);
        }
    },

    GOOGLE {
        @Override
        public OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes) {
            return new GoogleUserInfoResponse(attributes);
        }
    };

    /**
     * 각 프로바이더별로 구현 할 추상 메서드
     */
    public abstract OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes);

    public static SocialProvider fromString(String socialProvider) {
        return SocialProvider.valueOf(socialProvider.toUpperCase());
    }
}
