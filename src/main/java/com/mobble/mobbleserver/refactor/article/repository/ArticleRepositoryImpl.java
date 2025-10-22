package com.mobble.mobbleserver.refactor.article.repository;

import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.entity.ArticleType;
import com.mobble.mobbleserver.refactor.article.repository.dto.ArticleLikeInfoDto;
import com.mobble.mobbleserver.refactor.article.repository.dto.ArticleLikeProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mobble.mobbleserver.domain.like.articleLike.QArticleLike.articleLike;
import static com.mobble.mobbleserver.refactor.article.entity.QArticle.article;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Article> findArticlesByClubId(Long clubId, ArticleType articleType) {
        return queryFactory
                .selectFrom(article)
                .where(
                        article.club.id.eq(clubId),
                        articleType != null ? article.articleType.eq(articleType) : null
                )
                .orderBy(article.createdAt.desc())
                .fetch();
    }

    @Override
    public Map<Long, ArticleLikeInfoDto> findLikeInfoByArticleIdsAndMemberId(List<Long> articleIds, Long memberId) {
        List<ArticleLikeProjection> results = queryFactory
                .select(Projections.constructor(ArticleLikeProjection.class,
                        articleLike.article.id,
                        articleLike.member.id
                ))
                .from(articleLike)
                .where(articleLike.article.id.in(articleIds))
                .fetch();

        Map<Long, Integer> likeCountMap = createLikeCountMap(results);
        Set<Long> likedArticleIds = (memberId == null)
                ? Collections.emptySet()
                : extractLikedArticleIds(results, memberId);

        return articleIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> ArticleLikeInfoDto.toDto(
                                likeCountMap.getOrDefault(id, 0),
                                likedArticleIds.contains(id)
                        )
                ));
    }

    @Override
    public List<Long> findArticleIdsByClubId(Long clubId) {
        return queryFactory.select(article.id)
                .from(article)
                .where(article.club.id.eq(clubId))
                .fetch();
    }

    private Map<Long, Integer> createLikeCountMap(List<ArticleLikeProjection> results) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        ArticleLikeProjection::articleId,
                        Collectors.collectingAndThen(Collectors.toSet(), Set::size)
                ));
    }

    private Set<Long> extractLikedArticleIds(List<ArticleLikeProjection> results, Long memberId) {
        return results.stream()
                .filter(p -> p.memberId().equals(memberId))
                .map(ArticleLikeProjection::articleId)
                .collect(Collectors.toSet());
    }
}
