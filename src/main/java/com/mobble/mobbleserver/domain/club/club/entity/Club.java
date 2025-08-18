package com.mobble.mobbleserver.domain.club.club.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_category_id")
    private ClubCategory clubCategory;

    @Column(name = "name")
    private String name;

//    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
//    private final List<ClubImage> clubImages = new ArrayList<>();

    // Todo: 지역관리를 위해 추후 Enum 또는 테이블로 관리해야함.
    @Column(name = "ground")
    private String ground;

    @Column(name = "address")
    private String address;

    @Column(name = "head_count")
    private int headCount;

    @Column(name = "isAutoJoin")
    private boolean isAutoJoin;

    @Builder(access = AccessLevel.PRIVATE)
    private Club(
            ClubCategory category,
            String name,
            String ground,
            String address,
            int headCount,
            boolean isAutoJoin
    ) {
        validateCommon(category, name, ground, address, headCount);
        this.clubCategory = category;
        this.name = name;
        this.ground = ground;
        this.address = address;
        this.headCount = headCount;
        this.isAutoJoin = isAutoJoin;
    }

    public static Club createClub(
            ClubCategory category,
            String name,
            String ground,
            String address,
            int headCount,
            boolean isAutoJoin
    ) {
        return Club.builder()
                .category(category)
                .name(name)
                .ground(ground)
                .address(address)
                .headCount(headCount)
                .isAutoJoin(isAutoJoin)
                .build();
    }

    public void updateClub(
            ClubCategory category,
            String name,
            String ground,
            String address,
            int headCount,
            boolean isAutoJoin
    ) {
        validateCommon(category, name, ground, address, headCount);
        this.clubCategory = category;
        this.name = name;
        this.ground = ground;
        this.address = address;
        this.headCount = headCount;
        this.isAutoJoin = isAutoJoin;
    }

    private void validateCommon(
            ClubCategory category,
            String name,
            String ground,
            String address,
            int headCount
    ) {
        if (category == null) throw new DomainException(ClubErrorCode.CATEGORY_REQUIRED);
        if (name == null || name.isBlank()) throw new DomainException(ClubErrorCode.NAME_REQUIRED);
        if (ground == null || ground.isBlank()) throw new DomainException(ClubErrorCode.GROUND_REQUIRED);
        if (address == null || address.isBlank()) throw new DomainException(ClubErrorCode.ADDRESS_REQUIRED);
        if (headCount <= 0) throw new DomainException(ClubErrorCode.HEADCOUNT_REQUIRED);
    }
}
