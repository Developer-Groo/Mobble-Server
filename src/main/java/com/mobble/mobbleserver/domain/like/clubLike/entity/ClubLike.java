package com.mobble.mobbleserver.domain.like.clubLike.entity;

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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "club_id")
//    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

//Todo Club 생성 후 import
//    @Builder
//    private ClubLike(Club club, Member member) {
//        this.club = club;
//        this.member = member;
//    }

//    public static ClubLike createClubLike(Club club, Member member) {
//        if (club == null) throw new DomainException(LikeErrorCode.CLUB_REQUIRED);
//        if (member == null) throw new DomainException(LikeErrorCode.MEMBER_REQUIRED);
//
//        return ClubLike.builder()
//                .club(club)
//                .member(member)
//                .build();
//    }
}
