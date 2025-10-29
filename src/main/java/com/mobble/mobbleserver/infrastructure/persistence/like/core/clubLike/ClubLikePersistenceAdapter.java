package com.mobble.mobbleserver.infrastructure.persistence.like.core.clubLike;

import com.mobble.mobbleserver.application.liked.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.liked.port.required.LikeWritePort;
import com.mobble.mobbleserver.domain.like.core.ClubLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("clubLikePersistenceAdapter")
@RequiredArgsConstructor
public class ClubLikePersistenceAdapter implements LikeWritePort<ClubLike>, LikeReadPort<ClubLike> {

    private final JpaClubLikeRepository jpaClubLikeRepository;

    /**
     * LikeWritePort
     */
    @Override
    public ClubLike save(ClubLike clubLike) {
        return jpaClubLikeRepository.save(clubLike);
    }

    @Override
    public void delete(ClubLike clubLike) {
        jpaClubLikeRepository.delete(clubLike);
    }

    /**
     * LikeReadPort
     */
    @Override
    public Optional<ClubLike> findLike(Long targetId, Long memberId) {
        return jpaClubLikeRepository.findLikedByClubIdAndMemberId(targetId, memberId);
    }
}
