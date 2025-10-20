package com.mobble.mobbleserver.refactor.like.articleLike.service;

import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.like.articleLike.ArticleLike;
import com.mobble.mobbleserver.infrastructure.persistence.like.articleLike.JpaArticleLikeRepository;
import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.domain.like.baseLike.LikeType;
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
    private final JpaArticleLikeRepository articleLikeRepository;

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
