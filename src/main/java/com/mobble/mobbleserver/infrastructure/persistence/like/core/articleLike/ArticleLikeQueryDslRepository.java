package com.mobble.mobbleserver.infrastructure.persistence.like.core.articleLike;

import java.util.List;

public interface ArticleLikeQueryDslRepository {

    List<Long> findLikedMemberListByArticleId(Long articleId);

    List<Long> findLikedArticleIdListByMemberId(List<Long> articleIds, Long memberId);
}
