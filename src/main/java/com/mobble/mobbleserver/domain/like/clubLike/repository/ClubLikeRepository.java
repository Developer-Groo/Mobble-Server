package com.mobble.mobbleserver.domain.like.clubLike.repository;

import com.mobble.mobbleserver.domain.like.clubLike.entity.ClubLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubLikeRepository extends JpaRepository<ClubLike, Long> {

    Optional<ClubLike> findLikedByClubIdAndMemberId(Long clubId, Long memberId);

    int countClubLikesByClubId(Long clubId);

    boolean existsByClubIdAndMemberId(Long clubId, Long memberId);
}
