package com.mobble.mobbleserver.application.comment.port.required;

import com.mobble.mobbleserver.domain.comment.Comment;

import java.util.List;

public interface CommentWritePort {

    Comment save(Comment comment);

    void delete(Comment comment);

    void deleteAllByArticleId(Long articleId);

    void deleteAllByArticleIdIn(List<Long> articleIds);
}
