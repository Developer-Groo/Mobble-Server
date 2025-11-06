package com.mobble.mobbleserver.infrastructure.persistence.like.articleLike;

import com.mobble.mobbleserver.domain.like.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaArticleLikeRepository extends JpaRepository<ArticleLike, Long>, ArticleLikeQueryDslRepository {

    Optional<ArticleLike> findLikedByArticleIdAndMemberId(Long articleId, Long memberId);

    List<ArticleLike> findAllByArticleId(Long articleId);

    void deleteAllByArticleId(Long articleId);

    void deleteAllByArticleIdIn(List<Long> articleIds);

    boolean existsByMemberIdAndArticleId(Long memberId, Long articleId);

    void deleteByMemberIdAndArticleId(Long memberId, Long articleId);
}
