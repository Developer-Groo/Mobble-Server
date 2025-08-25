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

        // memberId 형태(Long)가 아니면 인증 대상 아님 (ex. signupToken)
        if (token != null && tokenProvider.validateToken(token)) {
            String subject = tokenProvider.parse(token).getSubject();

            if (!subject.matches("\\d+")) {
                filterChain.doFilter(request, response);
                return;
            }

            Long memberId = Long.valueOf(subject); // memberId 추출
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
