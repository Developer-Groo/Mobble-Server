package com.mobble.mobbleserver.application.liked.service.core;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.liked.port.required.LikeMemberListReadPort;
import com.mobble.mobbleserver.domain.like.core.ArticleLike;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeQueryService {

    @Qualifier("articleLikePersistenceAdapter")
    private final LikeMemberListReadPort<ArticleLike> likeMemberListReadPort;

    private final ArticleReadPort articleReadPort;

    public List<ArticleLike> getLikeEntities(Long articleId) {
        articleReadPort.findById(articleId);

        return likeMemberListReadPort.getLikeEntities(articleId);
    }
}
