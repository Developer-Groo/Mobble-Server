package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>,  ArticleQueryRepository {
    
    List<Article> findArticlesByClubId(Long clubId);
}
