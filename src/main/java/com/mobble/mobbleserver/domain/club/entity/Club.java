package com.mobble.mobbleserver.domain.club.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @Column(name = "club_category_id")
//    private ClubCategory clubCategory;

    private String name;

//    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
//    private final List<ClubImage> clubImages = new ArrayList<>();

    // Todo: 지역관리를 위해 추후 Enum 또는 테이블로 관리해야함.
    private String ground;

    private int headcount;

    private boolean joinType;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @Column(name = "chat_room_id")
//    private ChatRooom chatRooom;

    @Builder(access = AccessLevel.PRIVATE)
    private Club(
            // Todo: ClubCategory,ChatRooom 주입 필요
            String name,
            String ground,
            int headcount,
            boolean joinType
    ) {
        this.name = name;
        this.ground = ground;
        this.headcount = headcount;
        this.joinType = joinType;
    }

    public static Club createClub(
            // Todo: ClubCategory,ChatRooom 주입 필요
            String name,
            String ground,
            int headcount,
            boolean joinType
    ){
        return Club.builder()
                .name(name)
                .ground(ground)
                .headcount(headcount)
                .joinType(joinType)
                .build();
    }
}
