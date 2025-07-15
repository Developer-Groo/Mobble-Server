package com.mobble.mobbleserver.domain.clubCategory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubCategory {

    @Id
    @Column(name = "club_category_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubCategory(String name) {
        this.name = name;
    }

    public static ClubCategory createClubCategory(String name) {
        return ClubCategory.builder()
                .name(name)
                .build();
    }
}