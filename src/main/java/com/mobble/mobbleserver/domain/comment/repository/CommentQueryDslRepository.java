package com.mobble.mobbleserver.domain.comment.repository;

import com.mobble.mobbleserver.domain.comment.repository.dto.CommentLikeInfo;
import com.mobble.mobbleserver.domain.comment.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentQueryDslRepository {

    List<Comment> findCommentsWithRepliesByArticleId(Long articleId);

    Map<Long, CommentLikeInfo> findLikeInfoByCommentIdsAndMemberId(List<Long> commentIds, Long memberId);
}
