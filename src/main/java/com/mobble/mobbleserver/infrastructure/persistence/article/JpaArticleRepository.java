package com.mobble.mobbleserver.infrastructure.persistence.article;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByIdAndClubId(Long articleId, Long clubId);

    List<Article> findByClubId(Long clubId);

    List<Article> findByClubIdAndArticleType(Long clubId, ArticleType articleType);

    List<Long> findIdsByClubId(Long clubId);

    void deleteAllByClubId(Long clubId);
}
