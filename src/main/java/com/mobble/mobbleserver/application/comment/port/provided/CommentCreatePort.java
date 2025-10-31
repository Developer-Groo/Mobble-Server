package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.application.comment.command.request.CreateReplyCommentCommand;
import com.mobble.mobbleserver.application.comment.command.request.CreateRootCommentCommand;
import com.mobble.mobbleserver.domain.comment.Comment;

public interface CommentCreatePort {

    Comment createRootComment(CreateRootCommentCommand command);

    Comment createReplyComment(CreateReplyCommentCommand command);
}
