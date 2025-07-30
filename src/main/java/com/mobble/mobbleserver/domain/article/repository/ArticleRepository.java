package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.comment.repository.CommentQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>,  ArticleQueryDslRepository {
    
    List<Article> findArticlesByClubId(Long clubId);
}
