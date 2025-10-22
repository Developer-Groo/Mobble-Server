package com.mobble.mobbleserver.infrastructure.persistence.comment;

import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.comment.QComment;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeProjection;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mobble.mobbleserver.domain.comment.QComment.comment;
import static com.mobble.mobbleserver.domain.like.commentLike.QCommentLike.commentLike;

@RequiredArgsConstructor
public class CommentQueryDslRepositoryImpl implements CommentQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentsWithRepliesByArticleId(Long articleId) {
        QComment child = new QComment("child");

        return queryFactory
                .selectFrom(comment)
                .distinct()
                .leftJoin(comment.children, child)
                .fetchJoin()
                .where(
                        comment.article.id.eq(articleId),
                        comment.parent.isNull()
                )
                .orderBy(comment.createdAt.asc(), child.createdAt.asc())
                .fetch();
    }

    @Override
    public Map<Long, CommentLikeInfoDto> findLikeInfoByCommentIdsAndMemberId(List<Long> commentIds, Long memberId) {
        List<CommentLikeProjection> results = queryFactory
                .select(Projections.constructor(CommentLikeProjection.class,
                        commentLike.member.id,
                        commentLike.comment.id
                ))
                .from(commentLike)
                .where(commentLike.comment.id.in(commentIds))
                .fetch();

        Map<Long, Integer> likeCountMap = createLikeCountMap(results);
        Set<Long> likedCommentIds = (memberId == null)
                ? Collections.emptySet()
                : extractLikedCommentIds(results, memberId);

        return commentIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> CommentLikeInfoDto.toDto(
                                likeCountMap.getOrDefault(id, 0),
                                likedCommentIds.contains(id)
                        )
                ));
    }

    @Override
    public Map<Long, Integer> countCommentsByArticleIds(List<Long> articleIds) {
        List<Tuple> tuples = queryFactory
                .select(comment.article.id, comment.count())
                .from(comment)
                .where(comment.article.id.in(articleIds))
                .groupBy(comment.article.id)
                .fetch();

        return tuples.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, Long.class),
                        tuple -> tuple.get(1, Long.class).intValue()
                ));
    }

    private Map<Long, Integer> createLikeCountMap(List<CommentLikeProjection> results) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        CommentLikeProjection::commentId,
                        Collectors.collectingAndThen(
                                Collectors.toSet(),
                                Set::size
                        )
                ));
    }

    private Set<Long> extractLikedCommentIds(List<CommentLikeProjection> results, Long memberId) {
        return results.stream()
                .filter(projection -> projection.memberId().equals(memberId))
                .map(CommentLikeProjection::commentId)
                .collect(Collectors.toSet());
    }
}
