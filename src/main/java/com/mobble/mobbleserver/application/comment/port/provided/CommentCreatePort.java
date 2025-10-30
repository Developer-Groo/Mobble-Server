package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.domain.comment.Comment;

import static com.mobble.mobbleserver.application.comment.command.CommentCommand.CreateReplyCommentCommand;
import static com.mobble.mobbleserver.application.comment.command.CommentCommand.CreateRootCommentCommand;

public interface CommentCreatePort {

    Comment createRootComment(CreateRootCommentCommand command);

    Comment createReplyComment(CreateReplyCommentCommand command);
}
