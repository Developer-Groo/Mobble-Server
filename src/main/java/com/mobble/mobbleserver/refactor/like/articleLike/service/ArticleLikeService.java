package com.mobble.mobbleserver.refactor.like.articleLike.service;

import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import com.mobble.mobbleserver.refactor.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.refactor.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.refactor.like.baseLike.service.AbstractLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleLikeService extends AbstractLikeService<Article, ArticleLike> {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleValidator articleValidator;

    private final ClubMemberReadPort clubMemberReadPort;

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
        clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, member.getId());

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
}
