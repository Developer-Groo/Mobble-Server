package com.mobble.mobbleserver.article.repository;

import com.mobble.mobbleserver.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
