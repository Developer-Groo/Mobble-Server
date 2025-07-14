package com.mobble.mobbleserver.domain.comment.repository;

import com.mobble.mobbleserver.domain.comment.repository.dto.CommentLikeInfoDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentQueryDslRepository {

    List<Comment> findCommentsWithRepliesByArticleId(Long articleId);

    Map<Long, CommentLikeInfoDto> findLikeInfoByCommentIdsAndMemberId(List<Long> commentIds, Long memberId);
}
