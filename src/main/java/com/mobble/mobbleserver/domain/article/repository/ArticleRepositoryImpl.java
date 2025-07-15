package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.domain.article.dto.response.ArticleDetailDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mobble.mobbleserver.domain.article.entity.QArticle.article;
import static com.mobble.mobbleserver.domain.comment.entity.QComment.comment;
import static com.mobble.mobbleserver.domain.like.articleLike.entity.QArticleLike.articleLike;
import static com.mobble.mobbleserver.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ArticleSummaryResponseDto> findArticlesByClubId(Long clubId, ArticleType articleType) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(article.club.id.eq(clubId));
        if (articleType != null) {
            builder.and(article.articleType.eq(articleType));
        }

        return queryFactory
                .select(Projections.constructor(ArticleSummaryResponseDto.class,
                        article.id,
                        article.title,
                        article.content,
                        article.articleType,
                        article.club.id,
                        article.member.name,
                        articleLike.id.countDistinct(),
                        comment.id.countDistinct(),
                        article.createdAt,
                        article.updatedAt
                ))
                .from(article)
                .leftJoin(articleLike).on(articleLike.article.eq(article))
                .leftJoin(comment).on(comment.article.eq(article))
                .where(builder)
                .groupBy(
                        article.id,
                        article.title,
                        article.content,
                        article.articleType,
                        article.club.id,
                        article.member.name,
                        article.createdAt,
                        article.updatedAt
                )
                .orderBy(article.createdAt.desc())
                .fetch();
    }

    @Override
    public ArticleDetailDto findArticleDetailById(Long articleId) {

        return queryFactory
                .select(Projections.constructor(ArticleDetailDto.class,
                        article.id,
                        article.title,
                        article.content,
                        article.articleType,
                        article.club.id,
                        member.id,
                        member.name,
                        articleLike.id.countDistinct(),
                        comment.id.countDistinct(),
                        article.createdAt,
                        article.updatedAt
                ))
                .from(article)
                .leftJoin(article.member, member)
                .leftJoin(articleLike).on(articleLike.article.eq(article))
                .leftJoin(comment).on(comment.article.eq(article))
                .where(article.id.eq(articleId))
                .groupBy(article.id, member.id, member.name, article.club.id)
                .fetchOne();
    }
}
