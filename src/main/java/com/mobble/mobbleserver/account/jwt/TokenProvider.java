package com.mobble.mobbleserver.account.jwt;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class TokenProvider {

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /* =======================
     *  Create Token
     * ======================= */

    // Jwt Token 생성
    public String createJwtToken(Long memberId, List<ClubMemberRole> roles) {

        Date now = new Date();

        Map<String, Object> claims = new HashMap<>();
        if (roles != null && !roles.isEmpty()) claims.put("roles", roles.stream().map(Enum::name).toList());

        return Jwts.builder()
                .setSubject(memberId.toString())
                .addClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // roles 없이 토큰 생성 (클럽 미가입, 일반 유저용)
    public String createJwtToken(Long memberId) {
        return createJwtToken(memberId, List.of());
    }

    // 회원가입용 Signup Token 생성
    public String createSignupToken(String email, SocialProvider socialProvider, String socialId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000L * 60 * 10); // Valid Time: 10 minute

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("socialProvider", String.valueOf(socialProvider));
        claims.put("socialId", socialId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(socialProvider.name() + ":" + socialId)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /* =======================
     *  Parse / Validate
     * ======================= */

    // Access Token -> memberId
    public Optional<Long> getMemberIdByJwtToken(String token) {
        String subject = parse(token).getSubject();

        if (!subject.matches("\\d+")) {
            return Optional.empty();
        }

        return Optional.of(Long.valueOf(subject));
    }

    // Access Token -> roles (없으면 빈 리스트)
    public List<ClubMemberRole> getRolesByJwtToken(String jwtToken) {
        List<?> roles = parse(jwtToken).get("roles", List.class);
        if (roles == null) return List.of();
        return roles.stream()
                .map(String::valueOf)          // Object -> String
                .map(ClubMemberRole::valueOf)  // String -> Enum
                .toList();
    }

    // Signup Token -> SocialUserInfo
    public SocialUserInfo getSignupTokenInfo(String signupToken) {
        Claims claims = parse(signupToken);

        String email = (String) claims.get("email");
        SocialProvider socialProvider = SocialProvider.valueOf((String) claims.get("socialProvider"));
        String socialId = (String) claims.get("socialId");

        return new SocialUserInfo(email, socialProvider, socialId);
    }

    /* =======================
     *  Validate
     * ======================= */

    public boolean validateToken(String jwtToken) {
        try {
            parse(jwtToken);
            return true;
        } catch (Exception e) {
            // MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException
            log.warn("Invalid JWT token. reason: {}", e.getMessage());
            return false;
        }
    }

    /* =======================
     *  Internal
     * ======================= */

    Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
