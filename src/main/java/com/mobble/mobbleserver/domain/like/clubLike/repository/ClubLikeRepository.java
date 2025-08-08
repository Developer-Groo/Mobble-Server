package com.mobble.mobbleserver.domain.like.clubLike.repository;

import com.mobble.mobbleserver.domain.like.clubLike.entity.ClubLike;
import com.mobble.mobbleserver.domain.like.repository.GenericLikeRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface ClubLikeRepository extends GenericLikeRepository<ClubLike> {

    Optional<ClubLike> findLikedByClubIdAndMemberId(Long clubId, Long memberId);

    @Modifying
    void deleteClubLikeAllByClub_Id(Long clubId);
}
