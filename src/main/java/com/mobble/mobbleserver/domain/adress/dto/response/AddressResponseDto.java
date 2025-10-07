package com.mobble.mobbleserver.domain.adress.dto.response;

import com.mobble.mobbleserver.domain.adress.entity.Address;

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
