package com.mobble.mobbleserver.infrastructure.persistence.comment;

import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;

import java.util.List;
import java.util.Map;

public interface CommentQueryDslRepository {

    List<Comment> findCommentsWithRepliesByArticleId(Long articleId);

    Map<Long, CommentLikeInfoDto> findLikeInfoByCommentIdsAndMemberId(List<Long> commentIds, Long memberId);

    // Todo: Comment 도메인의 역할이 아님 수정 필요: 호진
    Map<Long, Integer> countCommentsByArticleIds(List<Long> articleIds);
}
