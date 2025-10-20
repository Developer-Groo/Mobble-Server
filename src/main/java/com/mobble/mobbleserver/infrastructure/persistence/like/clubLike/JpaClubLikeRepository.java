package com.mobble.mobbleserver.infrastructure.persistence.like.clubLike;

import com.mobble.mobbleserver.domain.like.clubLike.ClubLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface JpaClubLikeRepository extends JpaRepository<ClubLike, Long> {

    Optional<ClubLike> findLikedByClubIdAndMemberId(Long clubId, Long memberId);

    @Modifying
    void deleteClubLikeAllByClub_Id(Long clubId);
}
