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
//    @JoinColumn(name = "club_category_id")
//    private ClubCategory clubCategory;

    @Column(name = "name")
    private String name;

//    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
//    private final List<ClubImage> clubImages = new ArrayList<>();

    // Todo: 지역관리를 위해 추후 Enum 또는 테이블로 관리해야함.
    @Column(name = "ground")
    private String ground;

    @Column(name = "head_count")
    private int headCount;

    @Column(name = "join_type")
    private boolean joinType;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chat_room_id")
//    private ChatRooom chatRooom;

    @Builder(access = AccessLevel.PRIVATE)
    private Club(
            // Todo: ClubCategory,ChatRooom 주입 필요
            String name,
            String ground,
            int headCount,
            boolean joinType
    ) {
        this.name = name;
        this.ground = ground;
        this.headCount = headCount;
        this.joinType = joinType;
    }

    public static Club createClub(
            // Todo: ClubCategory,ChatRooom 주입 필요
            String name,
            String ground,
            int headCount,
            boolean joinType
    ){
        return Club.builder()
                .name(name)
                .ground(ground)
                .headCount(headCount)
                .joinType(joinType)
                .build();
    }
}
