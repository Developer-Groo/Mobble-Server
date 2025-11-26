package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.application.comment.command.CreateReplyCommentCommand;
import com.mobble.mobbleserver.application.comment.command.CreateRootCommentCommand;
import com.mobble.mobbleserver.domain.comment.Comment;

public interface CommentCreatePort {

    Comment createRootComment(CreateRootCommentCommand command);

    Comment createReplyComment(CreateReplyCommentCommand command);
}
