package com.mobble.mobbleserver.domain.like.articleLike.repository;

import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    // 파라미터 Long타입으로 변경
    Optional<ArticleLike> findByArticleIdAndMemberId(Long articleId, Long memberId);

    int countByArticleId(Long articleId);

    List<ArticleLike> findAllMemberByArticleId(Long articleId);
}