package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

import java.util.List;

public interface CommentLikeQueryDslRepository {

    void deleteAllByArticleId(Long articleId);

    void deleteAllByArticleIds(List<Long> articleIds);

    List<Long> findLikedCommentIdListByMemberId(List<Long> commentIds, Long memberId);
}
