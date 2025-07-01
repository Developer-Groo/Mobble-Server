package com.mobble.mobbleserver.domain.comment.repository;

import com.mobble.mobbleserver.domain.comment.entity.Comment;

import java.util.List;

public interface CommentQueryDslRepository {

    List<Comment> findCommentsWithRepliesByArticleId(Long articleId);
}
