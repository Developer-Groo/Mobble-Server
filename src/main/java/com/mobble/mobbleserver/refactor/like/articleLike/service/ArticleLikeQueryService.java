package com.mobble.mobbleserver.refactor.like.articleLike.service;

import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import com.mobble.mobbleserver.refactor.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.refactor.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.refactor.like.baseLike.service.LikeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeQueryService implements LikeQueryService {

    private final ArticleValidator articleValidator;
    private final ArticleLikeRepository articleLikeRepository;

    @Override
    public LikeType getType() {
        return LikeType.ARTICLE;
    }

    @Override
    public LikeMemberListResponseDto getLikedMemberList(Long articleId) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByArticleId(article.getId());

        return LikeMemberListResponseDto.toDto(article.getId(), articleLikes, ArticleLike::getMember);
    }
}
