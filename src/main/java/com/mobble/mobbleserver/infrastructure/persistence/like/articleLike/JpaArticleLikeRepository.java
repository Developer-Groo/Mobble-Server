package com.mobble.mobbleserver.infrastructure.persistence.like.articleLike;

import com.mobble.mobbleserver.domain.like.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface JpaArticleLikeRepository extends JpaRepository<ArticleLike, Long>, ArticleLikeQueryDslRepository {

    boolean existsByMemberIdAndArticleId(Long memberId, Long articleId);

    @Modifying
    void deleteByMemberIdAndArticleId(Long memberId, Long articleId);

    @Modifying
    void deleteByArticleId(Long articleId);

    @Modifying
    void deleteAllByArticleIdIn(List<Long> articleIds);
}
