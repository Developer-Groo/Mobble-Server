package com.mobble.mobbleserver.refactor.like.clubLike.repository;

import com.mobble.mobbleserver.domain.like.core.ClubLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface ClubLikeRepository extends JpaRepository<ClubLike, Long> {

    Optional<ClubLike> findLikedByClubIdAndMemberId(Long clubId, Long memberId);

    @Modifying
    void deleteClubLikeAllByClub_Id(Long clubId);
}
