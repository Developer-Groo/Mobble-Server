package com.mobble.mobbleserver.account.auth.oauth.verifier.provider;

import com.mobble.mobbleserver.account.auth.oauth.dto.response.KakaoUserInfoResponse;
import com.mobble.mobbleserver.account.auth.oauth.verifier.AbstractSocialTokenVerifier;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class KakaoTokenVerifier extends AbstractSocialTokenVerifier {

    public KakaoTokenVerifier(RestClient kakaoRestClient) {
        super(kakaoRestClient);
    }

    @Override
    protected String getUserInfoUri() {
        return "/v2/user/me";
    }

    @Override
    protected SocialUserInfo parseUserInfo(Map<String, Object> attributes) {
        KakaoUserInfoResponse userInfo = new KakaoUserInfoResponse(attributes);

        return new SocialUserInfo(
                userInfo.getEmail(),
                userInfo.getProvider(),
                userInfo.getProviderId()
        );
    }
}
