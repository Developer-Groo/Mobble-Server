package com.mobble.mobbleserver.account.oauth2.service.handler;

import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.account.user.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String targetUrl;

        if (customUserDetails.isNewUser()) {
            // 신규 사용자 → 추가 정보 입력 페이지로 이동
            targetUrl = UriComponentsBuilder.fromUriString("/signup/details-info")
                    .queryParam("email", customUserDetails.getUsername())
                    .queryParam("name", customUserDetails.getName())
                    .queryParam("socialProvider", customUserDetails.getAttributes().get("socialProvider").toString())
                    .queryParam("socialId", customUserDetails.getAttributes().get("socialId").toString())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            log.info("신규 사용자입니다. 추가 정보 입력 페이지로 리다이렉트합니다: {}", targetUrl);
        } else {
            // 기존 사용자 → 토큰 생성 후 쿠키에 저장
            String accessToken = tokenProvider.createAccessToken(customUserDetails.member().getId());

            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .path("/")
                    .httpOnly(false) //Todo 배포시 true 변경
                    .secure(false) //Todo 배포시 true 변경
                    .sameSite("Lax") //Todo 배포시 "None" 변경
                    .maxAge(Duration.ofDays(1))
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());

            // 기존 사용자는 targetUrl로 리다이렉트 페이지로 이동
            //Todo: 테스트용, 추후 url 수정
            targetUrl = "http://localhost:8080/login-success.html";
            log.info("기존 사용자입니다. JWT Access Token: {}", accessToken);
        }

        // 리다이렉트 수행 (Spring Security 기본 redirect 사용)
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
