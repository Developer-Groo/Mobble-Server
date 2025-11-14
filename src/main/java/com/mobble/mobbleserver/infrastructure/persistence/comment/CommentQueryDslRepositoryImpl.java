package com.mobble.mobbleserver.infrastructure.persistence.comment;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mobble.mobbleserver.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentQueryDslRepositoryImpl implements CommentQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, Integer> countCommentsByArticleIds(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) return Map.of();

        List<Tuple> tuples = queryFactory
                .select(comment.article.id, comment.count())
                .from(comment)
                .where(comment.article.id.in(articleIds))
                .groupBy(comment.article.id)
                .fetch();

        return tuples.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(comment.article.id),
                        tuple -> Objects.requireNonNull(tuple.get(comment.count())).intValue()
                ));
    }
}
