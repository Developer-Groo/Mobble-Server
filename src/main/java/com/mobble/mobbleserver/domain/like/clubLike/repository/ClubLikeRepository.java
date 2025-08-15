package com.mobble.mobbleserver.domain.like.clubLike.repository;

import com.mobble.mobbleserver.domain.like.clubLike.entity.ClubLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ClubLikeRepository extends JpaRepository<ClubLike, Long> {

//    Optional<ClubLike> findLikedByClubIdAndMemberId(Long clubId, Long memberId);

//    int countClubLikesByClubId(Long clubId);

//    boolean existsByClubIdAndMemberId(Long clubId, Long memberId);

    @Modifying
    void deleteClubLikeAllByClub_Id(Long clubId);
}
