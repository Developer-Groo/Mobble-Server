package com.mobble.mobbleserver.domain.like.articleLike.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.like.articleLike.dto.response.ArticleLikeMemberListResponseDto;
import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.domain.like.baseLike.service.AbstractLikeService;
import com.mobble.mobbleserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleLikeService extends AbstractLikeService<Article, ArticleLike> {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleValidator articleValidator;
    private final ClubMemberValidator clubMemberValidator;

    @Override
    public LikeType getType() {
        return LikeType.ARTICLE;
    }

    @Override
    protected Article getTarget(Long targetId) {
        return articleValidator.findArticleByArticleIdOrThrow(targetId);
    }

    @Override
    protected Optional<ArticleLike> findExistingLike(Article article, Member member) {
        return articleLikeRepository.findLikedByArticleIdAndMemberId(article.getId(), member.getId());
    }

    @Override
    protected ArticleLike createLike(Article article, Member member) {
        Long clubId = article.getClub().getId();
        clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, member.getId());

        return ArticleLike.createArticleLike(article, member);
    }

    @Override
    protected void saveLike(ArticleLike entity) {
        articleLikeRepository.save(entity);
    }

    @Override
    protected void deleteLike(ArticleLike entity) {
        articleLikeRepository.delete(entity);
    }

    public ArticleLikeMemberListResponseDto getArticleLikedMembers(Long articleId) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByArticleId(article.getId());

        return ArticleLikeMemberListResponseDto.toDto(article.getId(), articleLikes);
    }
}
