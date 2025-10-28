package com.mobble.mobbleserver.refactor.like.clubLike.entity;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.AbstractLike;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubLike extends AbstractLike {

    @JoinColumn(name = "club_id", nullable = false)
    private Long clubId;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubLike(Long memberId, Long clubId) {
        super(memberId);
        if (clubId == null) throw new DomainException(LikeErrorCode.CLUB_REQUIRED);
        this.clubId = clubId;
    }

    public static ClubLike createClubLike(Long memberId, Long clubId) {
        return ClubLike.builder()
                .clubId(clubId)
                .memberId(memberId)
                .build();
    }
}
