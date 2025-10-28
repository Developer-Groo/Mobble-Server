package com.mobble.mobbleserver.refactor.like.articleLike.service;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.domain.like.articleLike.ArticleLike;
import com.mobble.mobbleserver.refactor.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.like.abstractLike.LikeType;
import com.mobble.mobbleserver.refactor.like.baseLike.service.AbstractLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleLikeService extends AbstractLikeService<Article, ArticleLike> {

    private final ArticleLikeRepository articleLikeRepository;

    private final ClubMemberReadPort clubMemberReadPort;
    private final ArticleReadPort articleReadPort;

    @Override
    public LikeType getType() {
        return LikeType.ARTICLE;
    }

    @Override
    protected Article getTarget(Long targetId) {
        return findArticleByArticleIdOrThrow(targetId);
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

    private Article findArticleByArticleIdOrThrow(Long articleId) {
        return articleReadPort.findById(articleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));
    }
}
