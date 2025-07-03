package com.mobble.mobbleserver.account.oauth2.provider;


import com.mobble.mobbleserver.account.oauth2.dto.response.KakaoUserInfoResponse;
import com.mobble.mobbleserver.account.oauth2.dto.response.OAuth2UserInfo;

import java.util.Map;

public enum SocialProvider {
    KAKAO {
        @Override
        public OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes) {
            return new KakaoUserInfoResponse(attributes);
        }
    };

//     TODO 카카오 구현 후 순서대로 진행 예정
/*
    NAVER {
    @Override
    public OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes) {
        return null;
    }
},
    GOOGLE{
        @Override
        public OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes) {
            return null;
        }
    },
    APPLE{
        @Override
        public OAuth2UserInfo getOAuth2UserInfo(Map<String, Object> attributes) {
            return null;
        }
    };
*/


    /**
     * Provider별 구현 할 추상 메서드
     */
    public abstract OAuth2UserInfo

    getOAuth2UserInfo(Map<String, Object> attributes);

    public static SocialProvider fromString(String providerName) {
        return SocialProvider.valueOf(providerName.toUpperCase());
    }
    }
