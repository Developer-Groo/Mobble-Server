package com.mobble.mobbleserver.infrastructure.persistence.article;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.article.port.required.ArticleWritePort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticlePersistenceAdapter implements ArticleReadPort, ArticleWritePort {

    private final JpaArticleRepository repository;

    /* ArticleWritePort */
    @Override
    public Article save(Article article) {
        return repository.save(article);
    }

    @Override
    public void delete(Article article) {
        repository.delete(article);
    }

    @Override
    public void deleteAll(Long clubId) {
        repository.deleteAllByClubId(clubId);
    }

    /* ArticleReadPort */
    @Override
    public Optional<Article> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Article> findByIdAndClubId(Long articleId, Long clubId) {
        return repository.findByIdAndClubId(articleId, clubId);
    }

    @Override
    public List<Article> findByClubId(Long clubId) {
        return repository.findByClubId(clubId);
    }

    @Override
    public List<Article> findByClubIdAndArticleType(Long clubId, ArticleType articleType) {
        return repository.findByClubIdAndArticleType(clubId, articleType);
    }

    @Override
    public List<Long> findIdsByClubId(Long clubId) {
        return repository.findIdsByClubId(clubId);
    }

    @Override
    public List<Long> findImageIdsByClubId(Long clubId) {
        return repository.findImageIdsByClubId(clubId);
    }
}
