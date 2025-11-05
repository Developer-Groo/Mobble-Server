package com.mobble.mobbleserver.infrastructure.persistence.like.core.commentLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mobble.mobbleserver.domain.comment.QComment.comment;
import static com.mobble.mobbleserver.domain.like.core.QCommentLike.commentLike;

@Repository
@RequiredArgsConstructor
public class CommentLikeQueryDslRepositoryImpl implements CommentLikeQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByArticleId(Long articleId) {
        List<Long> commentIds = queryFactory
                .select(comment.id)
                .from(comment)
                .where(comment.article.id.eq(articleId))
                .fetch();

        if (commentIds.isEmpty()) return;

        queryFactory
                .delete(commentLike)
                .where(commentLike.commentId.in(commentIds))
                .execute();
    }

    @Override
    public void deleteAllByArticleIds(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) return;

        queryFactory
                .delete(commentLike)
                .where(
                        commentLike.commentId.in(
                                com.querydsl.jpa.JPAExpressions
                                        .select(comment.id)
                                        .from(comment)
                                        .where(comment.article.id.in(articleIds))
                        )
                )
                .execute();
    }

    @Override
    public List<Long> findLikedCommentIdListByMemberId(List<Long> commentIds, Long memberId) {
        if (commentIds == null || commentIds.isEmpty()) return List.of();

        return queryFactory
                .select(commentLike.commentId)
                .from(commentLike)
                .where(
                        commentLike.commentId.in(commentIds),
                        commentLike.memberId.eq(memberId)
                )
                .fetch();
    }
}
