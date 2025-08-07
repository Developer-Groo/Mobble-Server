package com.mobble.mobbleserver.domain.like.commentLike.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mobble.mobbleserver.domain.comment.entity.QComment.comment;
import static com.mobble.mobbleserver.domain.like.commentLike.entity.QCommentLike.commentLike;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByArticleId(Long articleId) {
        queryFactory.delete(commentLike)
                .where(commentLike.comment.article.id.eq(articleId))
                .execute();
    }

    @Override
    public void deleteAllCommentLikeByArticleIds(List<Long> articleIds) {
        queryFactory.delete(commentLike)
                .where(commentLike.comment.article.id.in(articleIds))
                .execute();
    }
}
