package com.mobble.mobbleserver.account.oauth2.verifier;

import com.mobble.mobbleserver.account.oauth2.dto.response.GoogleUserInfoResponse;
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
                userInfo.getName(),
                userInfo.getEmail(),
                userInfo.getProvider(),
                userInfo.getProviderId()
        );
    }
}
