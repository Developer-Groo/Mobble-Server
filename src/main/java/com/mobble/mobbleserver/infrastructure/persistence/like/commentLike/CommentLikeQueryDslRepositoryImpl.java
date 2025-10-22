package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mobble.mobbleserver.domain.like.commentLike.QCommentLike.commentLike;

@Repository
@RequiredArgsConstructor
public class CommentLikeQueryDslRepositoryImpl implements CommentLikeQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByArticleId(Long articleId) {
        queryFactory.delete(commentLike)
                .where(commentLike.comment.article.id.eq(articleId))
                .execute();
    }
}
