package com.mobble.mobbleserver.domain.clubLikes.entity;

import com.mobble.mobbleserver.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_like_id")
    private Long id;

//Todo cascade, orphanRemoval = true 설정 필요
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "club_id", nullable = false)
//    private Club clubId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member memberId;

//Todo Club 생성 후 import
//    @Builder
//    private ClubLike(Club clubId, Member memberId) {
//        this.clubId = clubId;
//        this.memberId = memberId;
//    }

//    public static ClubLike createClubLike(Club clubId, Member memberId) {
//        return ClubLike.builder()
//                .clubId(clubId)
//                .memberId(memberId)
//                .build();
//    }
}
