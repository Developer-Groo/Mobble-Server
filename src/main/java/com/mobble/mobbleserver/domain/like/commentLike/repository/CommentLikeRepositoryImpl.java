package com.mobble.mobbleserver.domain.like.commentLike.repository;

import com.mobble.mobbleserver.domain.like.commentLike.entity.QCommentLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public void deleteAllByArticleId(Long articleId) {
        QCommentLike commentLike = QCommentLike.commentLike;

        queryFactory.delete(commentLike)
                .where(commentLike.comment.article.id.eq(articleId))
                .execute();
    }
}
