package com.mobble.mobbleserver.infrastructure.persistence.like.articleLike;

import com.mobble.mobbleserver.domain.like.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaArticleLikeRepository extends JpaRepository<ArticleLike, Long>, ArticleLikeQueryDslRepository {

    boolean existsByMemberIdAndArticleId(Long memberId, Long articleId);

    void deleteByMemberIdAndArticleId(Long memberId, Long articleId);

    void deleteByArticleId(Long articleId);

    void deleteAllByArticleIdIn(List<Long> articleIds);
}
