package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.application.comment.command.request.UpdateCommentCommand;
import com.mobble.mobbleserver.domain.comment.Comment;

public interface CommentUpdatePort {

    Comment updateComment(UpdateCommentCommand command);
}
