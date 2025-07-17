package com.mobble.mobbleserver.account.oauth2.service.handler;

import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.account.user.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String targetUrl;

        if (customUserDetails.isNewUser()) {
            targetUrl = UriComponentsBuilder.fromUriString("/signup/details-info")
                    .queryParam("email", customUserDetails.getUsername())
                    .queryParam("name", customUserDetails.getName())
                    .queryParam("provider", customUserDetails.getAttributes().get("provider").toString())
                    .queryParam("socialId", customUserDetails.getAttributes().get("socialId").toString())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            log.info("신규 사용자입니다. 추가 정보 입력 페이지로 리다이렉트합니다: {}", targetUrl);
        } else {
            String accessToken = tokenProvider.createAccessToken(customUserDetails.member().getId());

            targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect")
                    .queryParam("accessToken", accessToken)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            log.info("기존 사용자입니다. JWT 토큰을 발급하여 리다이렉트합니다: {}", targetUrl);
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
