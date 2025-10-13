package com.mobble.mobbleserver.refactor.like.commentLike.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mobble.mobbleserver.refactor.like.commentLike.entity.QCommentLike.commentLike;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByArticleId(Long articleId) {
        queryFactory.delete(commentLike)
                .where(commentLike.comment.article.id.eq(articleId))
                .execute();
    }
}
