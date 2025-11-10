package com.mobble.mobbleserver.infrastructure.persistence.article;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.article.port.required.ArticleWritePort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.infrastructure.persistence.article.projection.ArticleLikeInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
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
    public void deleteAllByClubId(Long clubId) {
        repository.deleteAllByClubId(clubId);
    }

    /* ArticleReadPort */
    @Override
    public Optional<Article> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Long> findIdsByClubId(Long clubId) {
        return repository.findIdsByClubId(clubId);
    }

    @Override
    public Optional<Article> findByIdAndClubId(Long articleId, Long clubId) {
        return repository.findByIdAndClubId(articleId, clubId);
    }

    @Override
    public boolean existsArticleByIdAndMemberId(Long articleId, Long memberId) {
        return repository.existsArticleByIdAndMemberId(articleId, memberId);
    }

    @Override
    public List<Article> findArticlesByClubId(Long clubId, ArticleType articleType) {
        return repository.findArticlesByClubId(clubId, articleType);
    }

    @Override
    public Map<Long, ArticleLikeInfoDto> findLikeInfoByArticleIdsAndMemberId(List<Long> articleIds, Long memberId) {
        return repository.findLikeInfoByArticleIdsAndMemberId(articleIds, memberId);
    }
}
