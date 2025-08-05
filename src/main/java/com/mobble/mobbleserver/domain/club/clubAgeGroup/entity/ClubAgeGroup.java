package com.mobble.mobbleserver.domain.clubAgeGroup.entity;

import com.mobble.mobbleserver.domain.club.entity.Club;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubAgeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_age_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    @Column(name = "club_age_group_type")
    private ClubAgeGroupType ageGroupType;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubAgeGroup(Club club,ClubAgeGroupType clubAgeGroupType){
        this.club = club;
        this.ageGroupType = clubAgeGroupType;
    }

    public static ClubAgeGroup createClubAgeGroup(Club club,ClubAgeGroupType clubAgeGroupType){
        return ClubAgeGroup.builder()
                .club(club)
                .clubAgeGroupType(clubAgeGroupType)
                .build();
    }
}
