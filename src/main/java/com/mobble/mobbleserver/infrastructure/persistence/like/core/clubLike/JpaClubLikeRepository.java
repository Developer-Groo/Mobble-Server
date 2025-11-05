package com.mobble.mobbleserver.infrastructure.persistence.like.core.clubLike;

import com.mobble.mobbleserver.domain.like.core.ClubLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface JpaClubLikeRepository extends JpaRepository<ClubLike, Long>, ClubLikeQueryDslRepository {

    Optional<ClubLike> findLikedByClubIdAndMemberId(Long clubId, Long memberId);

    @Modifying
    void deleteAllByClubId(Long clubId);

    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);

    void deleteByMemberIdAndClubId(Long memberId, Long clubId);
}
