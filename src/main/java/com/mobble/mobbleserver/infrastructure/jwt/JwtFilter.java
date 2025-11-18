package com.mobble.mobbleserver.infrastructure.jwt;

import com.mobble.mobbleserver.application.account.required.JwtTokenVerifierPort;
import com.mobble.mobbleserver.infrastructure.jwt.principal.AuthMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
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
import java.util.Optional;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenVerifierPort jwtTokenVerifierPort;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = resolveToken(request);

        if (jwtToken != null && jwtTokenVerifierPort.isValid(jwtToken)) {

            // Jwt 에서 memberId 추출 (subject 가 Long 이 아니면 Optional.empty 반환)
            Optional<Long> optionalMemberId = jwtTokenVerifierPort.extractMemberId(jwtToken);

            // memberId 형태(Long)가 아니면 인증 대상이 아니므로 필터 체인 통과 (ex.signupToken)
            if (optionalMemberId.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            Long memberId = optionalMemberId.get();
            List<ClubMemberRole> roles = jwtTokenVerifierPort.extractRoles(jwtToken);

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
