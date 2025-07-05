package com.mobble.mobbleserver.domain.like.articleLike.repository;

import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findLikedByArticleIdAndMemberId(Long articleId, Long memberId);

    List<ArticleLike> findAllByArticleId(Long articleId);

    boolean existsByArticleIdAndMemberId(Long articleId, Long memberId);
}