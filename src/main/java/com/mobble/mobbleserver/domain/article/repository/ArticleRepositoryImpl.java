package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.domain.article.entity.QArticle;
import com.mobble.mobbleserver.domain.club.entity.QClub;
import com.mobble.mobbleserver.domain.comment.entity.QComment;
import com.mobble.mobbleserver.domain.like.articleLike.entity.QArticleLike;
import com.mobble.mobbleserver.domain.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ArticleSummaryResponseDto> findArticlesByClubId(Long clubId) {
        QArticle article = QArticle.article;
        QArticleLike like = QArticleLike.articleLike;
        QComment comment = QComment.comment;
        QClub club = QClub.club;
        QMember member = QMember.member;


        return queryFactory
                .select(Projections.constructor(ArticleSummaryResponseDto.class,
                        article.id,
                        article.title,
                        article.content,
                        article.articleType,
                        article.club.id,
                        article.member.name,
                        like.id.countDistinct(),
                        comment.id.countDistinct(),
                        article.createdAt,
                        article.updatedAt
                ))
                .from(article)
                .leftJoin(like).on(like.article.eq(article))
                .leftJoin(comment).on(comment.article.eq(article))
                .where(article.club.id.eq(clubId))
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
                .fetch();
    }
}
