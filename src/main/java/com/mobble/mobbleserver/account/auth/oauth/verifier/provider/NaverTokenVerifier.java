package com.mobble.mobbleserver.account.auth.oauth.verifier.provider;

import com.mobble.mobbleserver.account.auth.oauth.dto.response.NaverUserInfoResponse;
import com.mobble.mobbleserver.account.auth.oauth.verifier.AbstractSocialTokenVerifier;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class NaverTokenVerifier extends AbstractSocialTokenVerifier {

    public NaverTokenVerifier(RestClient naverRestClient) {
        super(naverRestClient);
    }

    @Override
    protected String getUserInfoUri() {
        return "/v1/nid/me";
    }

    @Override
    protected SocialUserInfo parseUserInfo(Map<String, Object> attributes) {
        NaverUserInfoResponse userInfo = new NaverUserInfoResponse(attributes);

        return new SocialUserInfo(
                userInfo.getName(),
                userInfo.getEmail(),
                userInfo.getProvider(),
                userInfo.getProviderId()
        );
    }
}
