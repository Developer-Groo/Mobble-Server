package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>,  ArticleQueryRepository {
    
    List<Article> findArticlesByClubId(Long clubId);

    @Modifying
    void deleteAllArticleByClub_Id(Long clubId);
}
