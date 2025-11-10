package com.mobble.mobbleserver.infrastructure.persistence.like.clubLike;

import com.mobble.mobbleserver.domain.like.ClubLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaClubLikeRepository extends JpaRepository<ClubLike, Long>, ClubLikeQueryDslRepository {

    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);

    void deleteByMemberIdAndClubId(Long memberId, Long clubId);

    void deleteByClubId(Long clubId);
}
