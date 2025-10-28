package com.mobble.mobbleserver.refactor.like.articleLike.service;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.domain.like.core.ArticleLike;
import com.mobble.mobbleserver.refactor.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.refactor.like.baseLike.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.domain.like.core.LikeType;
import com.mobble.mobbleserver.refactor.like.baseLike.service.LikeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeQueryService implements LikeQueryService {

    private final ArticleLikeRepository articleLikeRepository;

    private final ArticleReadPort articleReadPort;

    @Override
    public LikeType getType() {
        return LikeType.ARTICLE;
    }

    @Override
    public LikeMemberListResponseDto getLikedMemberList(Long articleId) {
        Article article = articleReadPort.findById(articleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByArticleId(article.getId());

        return LikeMemberListResponseDto.toDto(article.getId(), articleLikes, ArticleLike::getMember);
    }
}
