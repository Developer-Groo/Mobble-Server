package com.mobble.mobbleserver.account.oauth2.service;

import com.mobble.mobbleserver.account.oauth2.dto.response.OAuth2UserInfo;
import com.mobble.mobbleserver.account.user.CustomUserDetails;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberValidator memberValidator;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("Social attributes: {}", attributes);

        // 서비스에 따라 유저정보를 다른 방식으로 추출
        OAuth2UserInfo oAuth2UserInfo = SocialProvider
                .fromString(registrationId)
                .getOAuth2UserInfo(attributes);

        String email = oAuth2UserInfo.getEmail();

        return memberValidator.findIsDeletedFalseMemberByEmail(email)
                .map(member -> {
                    log.info("기존 회원입니다. Member Entity: {}", member.getEmail());
                    return new CustomUserDetails(member, false, attributes);
                })
                .orElseGet(() -> {
                    log.info("신규 회원입니다. Email: {}", email);
                    Map<String, Object> newAttributes = Map.of(
                            "email", oAuth2UserInfo.getEmail(),
                            "name", oAuth2UserInfo.getName(),
                            "socialProvider", registrationId,
                            "socialId", oAuth2UserInfo.getProviderId()
                    );
                    return new CustomUserDetails(null, true, newAttributes);
                });
    }
}
