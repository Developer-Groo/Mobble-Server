package com.mobble.mobbleserver.application.article.port.required;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;

import java.util.List;
import java.util.Optional;

public interface ArticleReadPort {

    Optional<Article> findById(Long id);

    Optional<Article> findByIdAndClubId(Long articleId, Long clubId);

    List<Article> findByClubId(Long clubId);

    List<Article> findByClubIdAndArticleType(Long clubId, ArticleType articleType);

    List<Long> findIdsByClubId(Long clubId);
}
