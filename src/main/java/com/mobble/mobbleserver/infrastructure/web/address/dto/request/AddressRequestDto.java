package com.mobble.mobbleserver.infrastructure.web.address.dto.request;

import com.mobble.mobbleserver.domain.address.Address;
import com.mobble.mobbleserver.domain.club.core.Club;

public record AddressRequestDto(
        String roadAddress,
        String jibunAddress,
        Double latitude,
        Double longitude,
        String detail
) {

    public Address toEntity(Club club) {
        return Address.createAddress(
                club,
                this.roadAddress,
                this.jibunAddress,
                this.latitude,
                this.longitude,
                this.detail
        );
    }
}
