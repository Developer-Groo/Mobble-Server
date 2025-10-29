package com.mobble.mobbleserver.infrastructure.persistence.like.core.articleLike;

import com.mobble.mobbleserver.domain.like.core.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findLikedByArticleIdAndMemberId(Long articleId, Long memberId);

    List<ArticleLike> findAllByArticleId(Long articleId);

    void deleteAllByArticleId(Long articleId);

    void deleteAllByArticleIdIn(List<Long> articleIds);
}
