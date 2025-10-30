package com.mobble.mobbleserver.infrastructure.persistence.like.core.articleLike;

import com.mobble.mobbleserver.application.liked.core.port.required.LikeMemberListReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeWritePort;
import com.mobble.mobbleserver.domain.like.core.ArticleLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("articleLikePersistenceAdapter")
@RequiredArgsConstructor
public class ArticleLikePersistenceAdapter implements LikeWritePort<ArticleLike>, LikeReadPort<ArticleLike>, LikeMemberListReadPort<ArticleLike> {

    private final JpaArticleLikeRepository jpaArticleLikeRepository;

    /**
     * LikeWritePort
     */
    @Override
    public ArticleLike save(ArticleLike articleLike) {
        return jpaArticleLikeRepository.save(articleLike);
    }

    @Override
    public void delete(ArticleLike articleLike) {
        jpaArticleLikeRepository.delete(articleLike);
    }

    /**
     * LikeReadPort
     */
    @Override
    public Optional<ArticleLike> findLike(Long targetId, Long memberId) {
        return jpaArticleLikeRepository.findLikedByArticleIdAndMemberId(targetId, memberId);
    }

    /**
     * LikeMemberListReadPort
     */
    @Override
    public List<ArticleLike> getLikeEntities(Long articleId) {
        return jpaArticleLikeRepository.findAllByArticleId(articleId);

    }
}
