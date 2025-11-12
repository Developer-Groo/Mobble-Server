package com.mobble.mobbleserver.application.comment.port.provided;

import java.util.List;

public interface CommentDeletePort {

    void delete(Long memberId, Long clubId, Long articleId, Long commentId);

    void deleteAll(Long clubId, Long articleId);

    void deleteAll(List<Long> articleIds);
}
