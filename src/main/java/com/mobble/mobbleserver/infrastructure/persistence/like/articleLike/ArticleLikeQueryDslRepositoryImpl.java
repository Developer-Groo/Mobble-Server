package com.mobble.mobbleserver.infrastructure.persistence.like.articleLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.like.QArticleLike.articleLike;


@RequiredArgsConstructor
public class ArticleLikeQueryDslRepositoryImpl implements ArticleLikeQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findLikedMemberListByArticleId(Long articleId) {
        if (articleId == null) return List.of();

        return queryFactory
                .select(articleLike.memberId)
                .from(articleLike)
                .where(articleLike.articleId.eq(articleId))
                .orderBy(articleLike.id.desc())
                .fetch();
    }

    @Override
    public List<Long> findLikedArticleIdListByMemberId(List<Long> articleIds, Long memberId) {
        if (articleIds == null || articleIds.isEmpty()) return List.of();

        return queryFactory
                .select(articleLike.articleId)
                .from(articleLike)
                .where(
                        articleLike.articleId.in(articleIds),
                        articleLike.memberId.eq(memberId)
                )
                .fetch();
    }
}
