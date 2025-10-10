package com.mobble.mobbleserver.domain.adress.dto.request;

import com.mobble.mobbleserver.domain.adress.entity.Address;
import com.mobble.mobbleserver.domain.club.core.entity.Club;

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
