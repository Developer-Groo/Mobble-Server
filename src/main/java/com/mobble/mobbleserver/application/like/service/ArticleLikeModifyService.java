package com.mobble.mobbleserver.application.like.service;

import com.mobble.mobbleserver.application.like.required.LikeReadPort;
import com.mobble.mobbleserver.application.like.required.LikeWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.like.articleLike.ArticleLike;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import com.mobble.mobbleserver.refactor.clubMember.validator.ClubMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleLikeModifyService {

    private final ArticleValidator articleValidator;
    private final ClubMemberValidator clubMemberValidator;

    private final MemberReadPort memberReadPort;

    @Qualifier("articleLikePersistenceAdapter")
    private final LikeReadPort<ArticleLike> likeReadPort;

    @Qualifier("articleLikePersistenceAdapter")
    private final LikeWritePort<ArticleLike> likeWritePort;

    public void toggleLike(Long articleId, Long memberId) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        Long clubId = article.getClub().getId();
        clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, member.getId());

        Optional<ArticleLike> existLike = likeReadPort.findLike(article.getId(), member.getId());

        if (existLike.isPresent()) {
            likeWritePort.delete(existLike.get());
        } else {
            likeWritePort.save(ArticleLike.createArticleLike(article, member));
        }
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
