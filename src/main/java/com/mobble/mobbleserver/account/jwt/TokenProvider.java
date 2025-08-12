package com.mobble.mobbleserver.account.jwt;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
public class TokenProvider {

    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access Token 생성
     */
    public String createAccessJwtToken(Long memberId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000L * 60 * 60 * 24); // Valid Time: 1day

        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 회원가입용 Signup Token 생성
     */
    public String createSignupToken(String name, String email, SocialProvider socialProvider, String socialId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000L * 60 * 10); // Valid Time: 10 minute

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("name", name);
        claims.put("email", email);
        claims.put("socialProvider", String.valueOf(socialProvider));
        claims.put("socialId", socialId);

        return Jwts.builder()
                .setSubject(socialProvider.name() + ":" + socialId)
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * access 토큰 검증, memberId 반환
     */
    public Long getAccessTokenInfo(String accessJwtToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessJwtToken)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * signup 토큰 검증
     */
    public SocialUserInfo getSignupTokenInfo(String signupToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(signupToken)
                .getBody();

        String name = (String) claims.get("name");
        String email = (String) claims.get("email");
        SocialProvider socialProvider = SocialProvider.valueOf((String) claims.get("socialProvider"));
        String socialId = (String) claims.get("socialId");

        return new SocialUserInfo(name, email, socialProvider, socialId);
    }

    /**
     * 토큰 유효성 검증
     * 현재 만료 여부만 검증. Security 적용 후 재발급 로직 구현 예정
     */
    public boolean validateToken(String accessJwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessJwtToken);
            return true;
        } catch (Exception e) {
            // MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException
            log.warn("Invalid JWT token. reason: {}", e.getMessage());
            return false;
        }
    }
}
