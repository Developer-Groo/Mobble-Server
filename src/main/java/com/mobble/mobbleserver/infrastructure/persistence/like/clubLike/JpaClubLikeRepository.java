package com.mobble.mobbleserver.infrastructure.persistence.like.clubLike;

import com.mobble.mobbleserver.domain.like.ClubLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface JpaClubLikeRepository extends JpaRepository<ClubLike, Long>, ClubLikeQueryDslRepository {

    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);

    @Modifying
    void deleteByMemberIdAndClubId(Long memberId, Long clubId);

    @Modifying
    void deleteByClubId(Long clubId);
}
