package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.domain.comment.Comment;

import static com.mobble.mobbleserver.application.comment.command.CommentCommand.UpdateCommentCommand;

public interface CommentUpdatePort {

    Comment updateComment(UpdateCommentCommand command);
}
