package com.mobble.mobbleserver.account.oauth2.client;

import com.mobble.mobbleserver.account.oauth2.dto.response.KakaoUserInfoResponse;
import com.mobble.mobbleserver.account.oauth2.dto.response.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 카카오 OAuth 인증 처리를 위한 외부 API 클라이언트
 * - access_token 요청
 * - 사용자 정보 요청
 *
 * 이 클래스는 외부 API 통신만 담당하고,
 * 도메인 로직은 OAuthService에서 처리한다.
 */
@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private static final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    /**
     * 인가코드를 이용해 카카오 access_token을 요청한다.
     *
     * @param code 카카오로부터 받은 인가코드
     * @return access_token 문자열
     */
    public String requestAccessToken(String code) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        // 요청 헤더 설정 (application/x-www-form-urlencoded 필수)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 바디 구성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // POST 요청 실행
        ResponseEntity<Map> response = restTemplate.postForEntity(
                TOKEN_REQUEST_URL,
                request,
                Map.class
        );

        // 응답 코드 확인
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            assert body != null;

            return (String) body.get("access_token");
        }

        // 실패 시 예외 발생
        throw new IllegalStateException("카카오 access_token 요청 실패: " + response);
    }

    /**
     * access_token을 사용해 카카오 사용자 정보를 요청한다.
     *
     * @param accessToken 발급받은 카카오 access_token
     * @return 카카오 사용자 정보를 담은 OAuth2UserInfo 구현체
     */
    public OAuth2UserInfo requestUserInfo(String accessToken) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        // 요청 헤더 설정 (Authorization: Bearer ...)
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        // GET 요청 실행
        ResponseEntity<Map> response = restTemplate.exchange(
                USER_INFO_URL,
                HttpMethod.GET,
                request,
                Map.class
        );

        // 응답 성공 여부 확인
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            assert body != null;

            return new KakaoUserInfoResponse(body);
        }

        throw new IllegalStateException("카카오 사용자 정보 요청 실패: " + response);
    }
}