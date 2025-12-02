package com.mobble.mobbleserver.application.account.command;

import com.mobble.mobbleserver.domain.member.Gender;

public record SignUpCommand(
        String name,
        int age,
        Gender gender,
        String phone,
        Long profileImageId,

        String address1,
        String address2,
        String city,
        String district,
        Double latitude,
        Double longitude,

        boolean termsAgreed,
        boolean privacyAgreed,

        String signUpToken
) {

    public static SignUpCommand create(
            String name,
            int age,
            Gender gender,
            String phone,
            Long profileImageId,
            String address1,
            String address2,
            String city,
            String district,
            Double latitude,
            Double longitude,
            boolean termsAgreed,
            boolean privacyAgreed,
            String signUpToken
    ) {
        return new SignUpCommand(
                name,
                age,
                gender,
                phone,
                profileImageId,
                address1,
                address2,
                city,
                district,
                latitude,
                longitude,
                termsAgreed,
                privacyAgreed,
                signUpToken
        );
    }
}
