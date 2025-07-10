package com.mobble.mobbleserver.domain.ageGroup.entity;

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
    @Column(name = "age_group_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group_type")
    private AgeGroupType ageGroupType;

    @Builder(access = AccessLevel.PRIVATE)
    private AgeGroup(AgeGroupType ageGroupType){
        this.ageGroupType = ageGroupType;
    }

    public static AgeGroup createAgeGroup(AgeGroupType ageGroupType){
        return AgeGroup.builder()
                .ageGroupType(ageGroupType)
                .build();
    }
}
