package com.mobble.mobbleserver.config;

import com.mobble.mobbleserver.account.jwt.JwtFilter;
import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.account.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenProvider tokenProvider) throws Exception {

        // CSRF 보호 비활성화 (CSRF:악의적인 사이트에서 사용자의 인증된 세션을 악용해 요청을 보내는 공격)
        http
                .csrf(AbstractHttpConfigurer::disable);

        // Form 로그인 방식 비활성화
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // HTTP Basic 인증 방식 비활성화
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // oauth2 Login
        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                );

        // 경로별 인가 작업
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/members").permitAll()
                        .anyRequest().authenticated()
                );

        // 세션 설정: STATELESS
        http
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // JwtFilter 추가
        http
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
