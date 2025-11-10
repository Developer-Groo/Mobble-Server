package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

import java.util.List;

public interface CommentLikeQueryDslRepository {

    List<Long> findLikedCommentIdListByMemberId(List<Long> commentIds, Long memberId);
}
