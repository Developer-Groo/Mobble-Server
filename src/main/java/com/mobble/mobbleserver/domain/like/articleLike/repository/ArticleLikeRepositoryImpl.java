package com.mobble.mobbleserver.domain.like.articleLike.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mobble.mobbleserver.domain.like.articleLike.entity.QArticleLike.articleLike;

@Repository
@RequiredArgsConstructor
public class ArticleLikeRepositoryImpl implements ArticleLikeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllArticleLikeByArticleIds(List<Long> articleIds) {
        queryFactory.delete(articleLike)
                .where(articleLike.article.id.in(articleIds))
                .execute();
    }
}
