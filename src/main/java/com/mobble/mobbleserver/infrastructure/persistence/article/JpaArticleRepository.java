package com.mobble.mobbleserver.infrastructure.persistence.article;

import com.mobble.mobbleserver.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface JpaArticleRepository extends JpaRepository<Article, Long>, ArticleQueryDslRepository {

    Optional<Article> findArticleByIdAndMemberId(Long articleId, Long memberId);

    boolean existsArticleByIdAndMemberId(Long articleId, Long memberId);

    @Modifying
    void deleteAllArticleByClub_Id(Long clubId);
}
