package com.mobble.mobbleserver.domain.like;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubLike extends AbstractLike {

    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Builder(access = AccessLevel.PRIVATE)
    private ClubLike(Long memberId, Long clubId) {
        super(memberId);
        requireNonNull(clubId, "clubId must not be null");
        this.clubId = clubId;
    }

    public static ClubLike create(Long memberId, Long clubId) {
        return ClubLike.builder()
                .memberId(memberId)
                .clubId(clubId)
                .build();
    }
}
