package com.mobble.mobbleserver.account.auth.oauth.verifier.provider;

import com.mobble.mobbleserver.account.auth.oauth.dto.response.GoogleUserInfoResponse;
import com.mobble.mobbleserver.account.auth.oauth.verifier.AbstractSocialTokenVerifier;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class GoogleTokenVerifier extends AbstractSocialTokenVerifier {

    public GoogleTokenVerifier(RestClient googleRestClient) {
        super(googleRestClient);
    }

    @Override
    protected String getUserInfoUri() {
        return "/oauth2/v3/userinfo";
    }

    @Override
    protected SocialUserInfo parseUserInfo(Map<String, Object> attributes) {
        GoogleUserInfoResponse userInfo = new GoogleUserInfoResponse(attributes);

        return new SocialUserInfo(
                userInfo.getEmail(),
                userInfo.getProvider(),
                userInfo.getProviderId()
        );
    }
}
