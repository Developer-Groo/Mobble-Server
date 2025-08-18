package com.mobble.mobbleserver.account.jwt;

import com.mobble.mobbleserver.account.auth.oauth.service.SocialProvider;
import com.mobble.mobbleserver.account.auth.oauth.verifier.dto.SocialUserInfo;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
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
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TokenProvider {

    private final Key key;
    private final MemberValidator memberValidator;

    public TokenProvider(@Value("${jwt.secret}") String secretKey, MemberValidator memberValidator) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.memberValidator = memberValidator;
    }

    /* =======================
     *  Create Token
     * ======================= */

    // Access Jwt Token 생성
    public String createAccessJwtToken(Long memberId, List<ClubMemberRole> roles) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000L * 60 * 60 * 24); // Valid Time: 1day

        Map<String, Object> claims = new HashMap<>();
        if (roles != null && !roles.isEmpty()) claims.put("roles", roles.stream().map(Enum::name).toList());
        claims.put("tokenVersion", member.getTokenVersion());

        return Jwts.builder()
                .setSubject(memberId.toString())
                .addClaims(claims)
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    // roles 없이 토큰 생성 (클럽 미가입, 일반 유저용)
    public String createAccessJwtToken(Long memberId) {
        return createAccessJwtToken(memberId, List.of());
    }

    // 회원가입용 Signup Token 생성
    public String createSignupToken(String name, String email, SocialProvider socialProvider, String socialId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000L * 60 * 10); // Valid Time: 10 minute

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", name);
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
    public Long getMemberIdByJwtToken(String accessJwtToken) {
        return Long.valueOf(parse(accessJwtToken).getSubject());
    }

    // Access Token -> roles (없으면 빈 리스트)
    public List<ClubMemberRole> getRolesByJwtToken(String accessJwtToken) {
        List<?> roles = parse(accessJwtToken).get("roles", List.class);
        if (roles == null) return List.of();
        return roles.stream()
                .map(String::valueOf)          // Object -> String
                .map(ClubMemberRole::valueOf)  // String -> Enum
                .toList();
    }

    // Signup Token -> SocialUserInfo
    public SocialUserInfo getSignupTokenInfo(String signupToken) {
        Claims claims = parse(signupToken);

        String name = (String) claims.get("name");
        String email = (String) claims.get("email");
        SocialProvider socialProvider = SocialProvider.valueOf((String) claims.get("socialProvider"));
        String socialId = (String) claims.get("socialId");

        return new SocialUserInfo(name, email, socialProvider, socialId);
    }

    public int getTokenVersionByJwtToken(String accessJwtToken) {
        Object tokenVersion = parse(accessJwtToken).get("tokenVersion");

        if (tokenVersion == null) {
            throw new IllegalArgumentException("Missing tokenVersion in JWT"); //Todo 에러메시지 정의
        }

        if (tokenVersion instanceof Integer intValue) {
            return intValue;
        }

        if (tokenVersion instanceof String stringValue) {
            return Integer.parseInt(stringValue);
        }

        throw new IllegalArgumentException("Invalid tokenVersion type in JWT");
    }

    /* =======================
     *  Validate
     * ======================= */

    public boolean validateToken(String accessJwtToken) {
        try {
            parse(accessJwtToken);
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
