package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>,  ArticleQueryRepository {

    Optional<Article> findArticleByArticleIdAndMemberId(Long articleId, Long memberId);

    Boolean existsArticleByIdAndMemberId(Long articleId, Long memberId);

    @Modifying
    void deleteAllArticleByClub_Id(Long clubId);
}
