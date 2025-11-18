package com.mobble.mobbleserver.domain.club;

import com.mobble.mobbleserver.domain.common.BaseEntity;
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

    @Column(name = "name")
    private String name;

    @Column(name = "head_count")
    private int headCount;

    @Column(name = "isAutoJoin")
    private boolean isAutoJoin;

    @Builder(access = AccessLevel.PRIVATE)
    private Club(
            String name,
            int headCount,
            boolean isAutoJoin
    ) {
        this.name = name;
        this.headCount = headCount;
        this.isAutoJoin = isAutoJoin;
    }

    public static Club createClub(
            String name,
            int headCount,
            boolean isAutoJoin
    ) {
        return Club.builder()
                .name(name)
                .headCount(headCount)
                .isAutoJoin(isAutoJoin)
                .build();
    }

    public void updateClub(
            String name,
            int headCount,
            boolean isAutoJoin
    ) {
        this.name = name;
        this.headCount = headCount;
        this.isAutoJoin = isAutoJoin;
    }
}
