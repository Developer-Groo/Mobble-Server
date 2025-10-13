package com.mobble.mobbleserver.application.comment.port.required;

import com.mobble.mobbleserver.domain.comment.Comment;

public interface CommentWritePort {

    Comment save(Comment comment);

    void delete(Comment comment);
}
