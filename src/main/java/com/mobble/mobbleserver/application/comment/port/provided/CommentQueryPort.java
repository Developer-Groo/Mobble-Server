package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.application.comment.command.response.RootCommentResult;

import java.util.List;

public interface CommentQueryPort {

    List<RootCommentResult> getCommentListByArticle(Long articleId, Long memberId);
}
