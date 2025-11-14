package com.mobble.mobbleserver.infrastructure.persistence.comment;

import java.util.List;
import java.util.Map;

public interface CommentQueryDslRepository {

    Map<Long, Integer> countCommentsByArticleIds(List<Long> articleIds);
}
