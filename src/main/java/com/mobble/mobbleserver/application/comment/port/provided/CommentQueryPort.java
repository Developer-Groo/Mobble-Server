package com.mobble.mobbleserver.application.comment.port.provided;

import com.mobble.mobbleserver.application.comment.result.RootCommentResult;

import java.util.List;
import java.util.Map;

public interface CommentQueryPort {

    List<RootCommentResult> getCommentList(Long articleId, Long memberId);

    Map<Long, Integer> getCountComments(List<Long> articleIds);
}
