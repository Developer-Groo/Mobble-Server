package com.mobble.mobbleserver.application.like.service;

import com.mobble.mobbleserver.application.like.required.LikeMemberListReadPort;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeQueryService {

    private final ArticleValidator articleValidator;

    @Qualifier("articleLikePersistenceAdapter")
    private final LikeMemberListReadPort likeMemberListReadPort;

    public LikeMemberListResponseDto getMemberList(Long articleId) {
        articleValidator.findArticleByArticleIdOrThrow(articleId);

        return likeMemberListReadPort.getLikedMembers(articleId);
    }
}
