package com.mobble.mobbleserver.account.jwt;

import com.mobble.mobbleserver.account.auth.principal.AuthMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final MemberValidator memberValidator;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && tokenProvider.validateToken(token)) {
            Long memberId = tokenProvider.getMemberIdByJwtToken(token); // memberId 추출

            int tokenVersionFromToken = tokenProvider.getTokenVersionByJwtToken(token); // JWT Token Version
            int currentVersion = memberValidator.findMemberByMemberIdOrThrow(memberId).getTokenVersion(); // DB Token Version

            // 이전 버전의 토큰이므로 인증 실패 (401)
            // 클라이언트 측에서는 이 응답을 받으면 JWT를 삭제하고 로그인 페이지로 리다이렉트하는 등의 처리 필요
            if (tokenVersionFromToken < currentVersion) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 이전 버전 토큰인 경우 차단 (401 UnAuthorized)
                return;
            }

            List<ClubMemberRole> roles = tokenProvider.getRolesByJwtToken(token);
            Collection<? extends GrantedAuthority> authorities = roles.stream()
                    .map(clubMemberRole -> new SimpleGrantedAuthority(clubMemberRole.name()))
                    .toList();

            AuthMember authMember = new AuthMember(memberId);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(authMember, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }

        return null;
    }
}
