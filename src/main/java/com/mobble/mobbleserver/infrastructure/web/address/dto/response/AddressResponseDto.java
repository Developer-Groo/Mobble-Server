package com.mobble.mobbleserver.infrastructure.web.address.dto.response;

import com.mobble.mobbleserver.domain.address.Address;

public record AddressResponseDto(
        String roadAddress,
        String jibunAddress,
        Double latitude,
        Double longitude,
        String detail
) {
    public static AddressResponseDto toDto(Address address) {
        if (address == null) return null;
        return new AddressResponseDto(
                address.getRoadAddress(),
                address.getJibunAddress(),
                address.getLatitude(),
                address.getLongitude(),
                address.getDetail()
        );
    }
}
