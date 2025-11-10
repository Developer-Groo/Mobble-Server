package com.mobble.mobbleserver.infrastructure.persistence.article;

import com.mobble.mobbleserver.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaArticleRepository extends JpaRepository<Article, Long>, ArticleQueryDslRepository {

    Optional<Article> findByIdAndClubId(Long articleId, Long clubId);

    List<Long> findIdsByClubId(Long clubId);

    boolean existsArticleByIdAndMemberId(Long articleId, Long memberId);

    void deleteAllByClubId(Long clubId);
}
