package com.mobble.mobbleserver.refactor.adress.dto.request;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.refactor.adress.entity.Address;

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
