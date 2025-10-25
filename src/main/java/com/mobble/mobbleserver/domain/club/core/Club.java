package com.mobble.mobbleserver.domain.club.core;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.domain.address.Address;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.refactor.clubCategory.entity.ClubCategory;
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

    @OneToOne(mappedBy = "club", fetch = FetchType.LAZY)
    private Address address;

    @Column(name = "head_count")
    private int headCount;

    @Column(name = "isAutoJoin")
    private boolean isAutoJoin;

    @Builder(access = AccessLevel.PRIVATE)
    private Club(
            ClubCategory category,
            String name,
            int headCount,
            boolean isAutoJoin
    ) {
        validateCommon(category, name, headCount);
        this.clubCategory = category;
        this.name = name;
        this.headCount = headCount;
        this.isAutoJoin = isAutoJoin;
    }

    public static Club createClub(
            ClubCategory category,
            String name,
            int headCount,
            boolean isAutoJoin
    ) {
        return Club.builder()
                .category(category)
                .name(name)
                .headCount(headCount)
                .isAutoJoin(isAutoJoin)
                .build();
    }

    public void updateClub(
            ClubCategory category,
            String name,
            Address address,
            int headCount,
            boolean isAutoJoin
    ) {
        validateCommon(category, name, headCount);
        this.clubCategory = category;
        this.name = name;
        this.address = address;
        this.headCount = headCount;
        this.isAutoJoin = isAutoJoin;
    }

    private void validateCommon(
            ClubCategory category,
            String name,
            int headCount
    ) {
        if (category == null) throw new DomainException(ClubErrorCode.CATEGORY_REQUIRED);
        if (name == null || name.isBlank()) throw new DomainException(ClubErrorCode.NAME_REQUIRED);
        if (headCount <= 0) throw new DomainException(ClubErrorCode.HEADCOUNT_REQUIRED);
    }

    public void setAddress(Address address) {
        this.address = address;
        address.setClub(this);
    }
}
