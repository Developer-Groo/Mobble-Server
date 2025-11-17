package com.mobble.mobbleserver.domain.address;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.common.BaseEntity;
import com.mobble.mobbleserver.infrastructure.web.address.dto.request.AddressRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "jibun_address")
    private String jibunAddress;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "detail")
    private String detail;

    @Builder(access = AccessLevel.PRIVATE)
    private Address(
            Club club,
            String roadAddress,
            String jibunAddress,
            Double latitude,
            Double longitude,
            String detail
    ) {
        this.club = club;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detail = detail;
    }

    public static Address createAddress(
            Club club,
            String roadAddress,
            String jibunAddress,
            Double latitude,
            Double longitude,
            String detail
    ) {
        return Address.builder()
                .club(club)
                .roadAddress(roadAddress)
                .jibunAddress(jibunAddress)
                .latitude(latitude)
                .longitude(longitude)
                .detail(detail)
                .build();
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public void updateAddress(AddressRequestDto dto) {
        this.roadAddress = dto.roadAddress();
        this.jibunAddress = dto.jibunAddress();
        this.latitude = dto.latitude();
        this.longitude = dto.longitude();
        this.detail = dto.detail();
    }

}
