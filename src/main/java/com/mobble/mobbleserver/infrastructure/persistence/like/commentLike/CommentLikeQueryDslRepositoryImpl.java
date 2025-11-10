package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mobble.mobbleserver.domain.like.QCommentLike.commentLike;

@Repository
@RequiredArgsConstructor
public class CommentLikeQueryDslRepositoryImpl implements CommentLikeQueryDslRepository {

    private final JPAQueryFactory queryFactory;

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
