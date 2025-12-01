package com.mobble.mobbleserver.application.account.result;

public record SocialLoginResult(String jwtToken, boolean isNewMember) {

    public static SocialLoginResult existMember(String jwtToken) {
        return new SocialLoginResult(jwtToken, false);
    }

    public static SocialLoginResult newMember(String signupToken) {
        return new SocialLoginResult(signupToken, true);
    }
}

