package com.mobble.mobbleserver.domain.common;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    private String address1; // 도로명 주소
    private String address2; // 상세 주소
    private String city;     // 시/도
    private String district; // 구/군/동
    private Double latitude;
    private Double longitude;

    private Location(
            String address1,
            String address2,
            String city,
            String district,
            Double latitude,
            Double longitude
    ) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location create(
            String address1,
            String address2,
            String city,
            String district,
            Double latitude,
            Double longitude
    ) {
        // Todo: 유효서 검증 필요
        return new Location(address1, address2, city, district, latitude, longitude);
    }
}
