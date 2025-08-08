package com.mobble.mobbleserver.domain.like.clubLike.entity;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.like.entity.BaseLike;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "club_like_id"))
public class ClubLike extends BaseLike {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubLike(Club club, Member member) {
        validateCommon(club);
        this.club = club;
        assignMember(member);
    }

    public static ClubLike createClubLike(Club club, Member member) {
        return ClubLike.builder()
                .club(club)
                .member(member)
                .build();
    }

    private void validateCommon(Club club) {
        if (club == null) throw new DomainException(LikeErrorCode.CLUB_REQUIRED);
    }
}
