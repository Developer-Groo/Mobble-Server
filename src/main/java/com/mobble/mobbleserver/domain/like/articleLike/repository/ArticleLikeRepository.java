package com.mobble.mobbleserver.domain.like.articleLike.repository;

import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.domain.like.repository.GenericLikeRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface ArticleLikeRepository extends GenericLikeRepository<ArticleLike> {

    Optional<ArticleLike> findLikedByArticleIdAndMemberId(Long articleId, Long memberId);

    List<ArticleLike> findAllByArticleId(Long articleId);

    void deleteAllByArticleId(Long articleId);

    @Modifying
    void deleteAllArticleLikeByArticle_IdIn(List<Long> articleIds);
}
