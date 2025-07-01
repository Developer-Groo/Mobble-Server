package com.mobble.mobbleserver.domain.comment.repository;

import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.mobble.mobbleserver.domain.comment.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentQueryDslRepository {

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
                        // Todo: 구현 완료 되면 주석 해제
//                        comment.article.id.eq(articleId),
                        comment.parent.isNull()
                )
                .orderBy(comment.createdAt.asc(), child.createdAt.asc())
                .fetch();
    }
}
