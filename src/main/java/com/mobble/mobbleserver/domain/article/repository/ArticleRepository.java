package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
