package com.mobble.mobbleserver.domain.like.articleLike.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.like.articleLike.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.domain.like.baseLike.service.LikeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeQueryServiceImpl implements LikeQueryService {

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

        return LikeMemberListResponseDto.toDto(article.getId(), articleLikes);
    }
}
