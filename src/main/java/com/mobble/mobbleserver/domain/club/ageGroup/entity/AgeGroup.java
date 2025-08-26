package com.mobble.mobbleserver.domain.club.ageGroup.entity;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_age_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    @Column(name = "club_age_group_type")
    private AgeGroupType ageGroupType;

    @Builder(access = AccessLevel.PRIVATE)
    private AgeGroup(Club club, AgeGroupType ageGroupType) {
        this.club = club;
        this.ageGroupType = ageGroupType;
    }

    public static AgeGroup createAgeGroup(Club club, AgeGroupType ageGroupType) {
        return AgeGroup.builder()
                .club(club)
                .ageGroupType(ageGroupType)
                .build();
    }
}
