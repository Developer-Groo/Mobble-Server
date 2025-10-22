package com.mobble.mobbleserver.application.like.service;

import com.mobble.mobbleserver.application.like.required.LikeMemberListReadPort;
import com.mobble.mobbleserver.domain.like.articleLike.ArticleLike;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeQueryService {

    private final ArticleValidator articleValidator;

    @Qualifier("articleLikePersistenceAdapter")
    private final LikeMemberListReadPort<ArticleLike> likeMemberListReadPort;

    public List<ArticleLike> getLikeEntities(Long articleId) {
        articleValidator.findArticleByArticleIdOrThrow(articleId);

        return likeMemberListReadPort.getLikeEntities(articleId);
    }
}
