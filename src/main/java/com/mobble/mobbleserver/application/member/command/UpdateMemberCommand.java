package com.mobble.mobbleserver.application.member.command;

public record UpdateMemberCommand(
        Long memberId,

        Long profileImageId,

        String address1,
        String address2,
        String city,
        String district,
        Double latitude,
        Double longitude
) {

    public static UpdateMemberCommand create(
            Long memberId,
            Long profileImageId,
            String address1,
            String address2,
            String city,
            String district,
            Double latitude,
            Double longitude
    ) {
        return new UpdateMemberCommand(
                memberId,
                profileImageId,
                address1,
                address2,
                city,
                district,
                latitude,
                longitude
        );
    }
}
