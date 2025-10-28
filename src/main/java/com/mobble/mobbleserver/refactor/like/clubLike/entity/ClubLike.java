package com.mobble.mobbleserver.refactor.like.clubLike.entity;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.AbstractLike;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "club_like_id"))
public class ClubLike extends AbstractLike {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubLike(Club club, Member member) {
        if (club == null) throw new DomainException(LikeErrorCode.CLUB_REQUIRED);
        this.club = club;
        assignMember(member);
    }

    public static ClubLike createClubLike(Club club, Member member) {
        return ClubLike.builder()
                .club(club)
                .member(member)
                .build();
    }
}
