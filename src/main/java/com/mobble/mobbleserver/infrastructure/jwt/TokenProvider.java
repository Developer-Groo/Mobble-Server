package com.mobble.mobbleserver.infrastructure.jwt;

import com.mobble.mobbleserver.application.account.command.SocialProvider;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.application.account.required.JwtTokenIssuerPort;
import com.mobble.mobbleserver.application.account.required.JwtTokenVerifierPort;
import com.mobble.mobbleserver.application.account.required.SignUpTokenPort;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
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
public class TokenProvider implements JwtTokenIssuerPort, JwtTokenVerifierPort, SignUpTokenPort {

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /* =======================
     *  Create Token
     * ======================= */

    // Jwt Token 발급 (roles 포함)
    @Override
    public String issueJwtToken(Long memberId, List<ClubMemberRole> roles) {

        Date now = new Date();

        Map<String, Object> claims = new HashMap<>();
        if (roles != null && !roles.isEmpty())
            claims.put("roles", roles
                        .stream()
                        .map(Enum::name)
                        .toList());

        return Jwts.builder()
                .setSubject(memberId.toString())
                .addClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Jwt Token 발급 (roles 미포함)
    @Override
    public String issueJwtToken(Long memberId) {
        return issueJwtToken(memberId, List.of());
    }

    // 회원가입용 Signup Token 생성
    @Override
    public String issueSignUpToken(String email, SocialProvider socialProvider, String socialId) {
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

    // Jwt Token -> memberId 추출
    @Override
    public Optional<Long> extractMemberId(String token) {
        String subject = parse(token).getSubject();

        if (!subject.matches("\\d+")) {
            return Optional.empty();
        }

        return Optional.of(Long.valueOf(subject));
    }

    // Jwt Token -> roles 추출 (없으면 빈 리스트)
    @Override
    public List<ClubMemberRole> extractRoles(String jwtToken) {
        List<?> roles = parse(jwtToken).get("roles", List.class);
        if (roles == null) return List.of();
        return roles.stream()
                .map(String::valueOf)          // Object -> String
                .map(ClubMemberRole::valueOf)  // String -> Enum
                .toList();
    }

    // Signup Token -> SocialUserInfo 추출
    @Override
    public SocialUserInfo extractSignUpInfo(String signupToken) {
        Claims claims = parse(signupToken);

        String email = (String) claims.get("email");
        SocialProvider socialProvider = SocialProvider.valueOf((String) claims.get("socialProvider"));
        String socialId = (String) claims.get("socialId");

        return new SocialUserInfo(email, socialProvider, socialId);
    }

    /* =======================
     *  Validate
     * ======================= */

    @Override
    public boolean isValid(String jwtToken) {
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

    private Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
