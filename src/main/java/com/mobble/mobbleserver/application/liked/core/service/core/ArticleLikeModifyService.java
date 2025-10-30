package com.mobble.mobbleserver.application.liked.core.service.core;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.like.core.ArticleLike;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleLikeModifyService {

    @Qualifier("articleLikePersistenceAdapter")
    private final LikeReadPort<ArticleLike> likeReadPort;

    @Qualifier("articleLikePersistenceAdapter")
    private final LikeWritePort<ArticleLike> likeWritePort;

    private final MemberReadPort memberReadPort;
    private final ArticleReadPort articleReadPort;
    private final ClubMemberReadPort clubMemberReadPort;

    public void toggleLike(Long articleId, Long memberId) {
        Article article = findArticleByArticleIdOrThrow(articleId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        Long clubId = article.getClub().getId();
        validateClubMember(clubId, member.getId());

        Optional<ArticleLike> existLike = likeReadPort.findLike(article.getId(), member.getId());

        if (existLike.isPresent()) {
            likeWritePort.delete(existLike.get());
        } else {
            likeWritePort.save(ArticleLike.createArticleLike(member.getId(), article.getId()));
        }
    }

    private Article findArticleByArticleIdOrThrow(Long articleId) {
        return articleReadPort.findById(articleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    private void validateClubMember(Long clubId, Long memberId) {
        clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }
}
