package com.mobble.mobbleserver.domain.clubImage.entity;

import com.mobble.mobbleserver.common.baseEntity.CreatedAtEntity;
import com.mobble.mobbleserver.domain.club.entity.Club;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubImage extends CreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "url")
    private String url;

    @Column(name = "image_order")
    private int imageOrder;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubImage(Club club, String url, int imageOrder) {
        this.club = club;
        this.url = url;
        this.imageOrder = imageOrder;
    }

    public static ClubImage createClubImage(Club club, String url, int imageOrder) {
        return ClubImage.builder()
                .club(club)
                .url(url)
                .imageOrder(imageOrder)
                .build();
    }
}
