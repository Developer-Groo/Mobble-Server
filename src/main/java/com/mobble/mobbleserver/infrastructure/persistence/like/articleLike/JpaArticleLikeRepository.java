package com.mobble.mobbleserver.infrastructure.persistence.like.articleLike;

import com.mobble.mobbleserver.domain.like.articleLike.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface JpaArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findLikedByArticleIdAndMemberId(Long articleId, Long memberId);

    List<ArticleLike> findAllByArticleId(Long articleId);

    void deleteAllByArticleId(Long articleId);

    @Modifying
    void deleteAllArticleLikeByArticle_IdIn(List<Long> articleIds);
}
