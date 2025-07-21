package com.mobble.mobbleserver.account.oauth2.service.handler;

import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.account.user.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

            Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
            accessTokenCookie.setHttpOnly(false); //Todo: 배포 시 true 필수 * 자바스크립트에서 접근 불가 (XSS 보호)
            accessTokenCookie.setSecure(false);  // Todo: 배포 시 true 필수 * HTTPS에서만 전송
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge((int) Duration.ofDays(1).getSeconds()); // 1일 유효
            response.addCookie(accessTokenCookie);
            // Servlet Cookie API는 SameSite 설정 미지원 → 직접 헤더로 설정
            // SameSite=None: 크로스 사이트 요청에서도 쿠키 전송 허용 (앱 또는 프론트 분리된 경우 필요)

            //Todo: 배포 시 주석 해제
//            response.addHeader("Set-Cookie",
//                    "accessToken=" + accessToken +
//                            "; Path=/; HttpOnly; Secure; SameSite=None");

            // 기존 사용자는 메인 리다이렉트 페이지로 이동
            //Todo: 테스트용, 추후 url 수정
            targetUrl = "http://localhost:8080/login-success.html";
            log.info("기존 사용자입니다. JWT Access Token: {}", accessToken);
        }

        // 리다이렉트 수행 (Spring Security 기본 redirect 사용)
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
