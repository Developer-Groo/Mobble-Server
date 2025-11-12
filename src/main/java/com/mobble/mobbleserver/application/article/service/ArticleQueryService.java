package com.mobble.mobbleserver.application.article.service;

import com.mobble.mobbleserver.application.article.command.response.ArticleDetailResult;
import com.mobble.mobbleserver.application.article.command.response.ArticlePreviewResult;
import com.mobble.mobbleserver.application.article.error.ArticleBusinessError;
import com.mobble.mobbleserver.application.article.port.provided.ArticleQueryPort;
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.command.response.RootCommentResult;
import com.mobble.mobbleserver.application.comment.port.provided.CommentQueryPort;
import com.mobble.mobbleserver.application.common.exception.BusinessException;
import com.mobble.mobbleserver.application.like.port.provided.LikeQueryPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleQueryService implements ArticleQueryPort {

    private final CommentQueryPort commentQueryPort;
    private final LikeQueryPort likeQueryPort;

    private final ClubMemberReadPort clubMemberReadPort;
    private final MemberReadPort memberReadPort;
    private final ArticleReadPort articleReadPort;

    @Override
    public List<ArticlePreviewResult> getArticlesPreview(Long clubId, Long memberId, ArticleType articleType) {
        ClubMember clubMember = assertMemberByClubIdAndMemberId(clubId, memberId);
        Club club = clubMember.getClub();
        Member member = clubMember.getMember();

        List<Article> articles = (articleType == null)
                ? articleReadPort.findByClubId(club.getId())
                : articleReadPort.findByClubIdAndArticleType(club.getId(), articleType);

        List<Long> articleIds = articleReadPort.findIdsByClubId(club.getId());

        Map<Long, Long> likeCounts = likeQueryPort.getLikeCounts(LikeType.ARTICLE, articleIds);
        List<Long> likedIds = likeQueryPort.getLikedIds(LikeType.ARTICLE, member.getId(), articleIds);
        Map<Long, Integer> commentCounts = commentQueryPort.getCountComments(articleIds);

        // Todo: isOwner 필요
        return ArticlePreviewResult.create(articles, likeCounts, likedIds, commentCounts);
    }

    @Override
    public ArticleDetailResult getArticleDetail(Long clubId, Long articleId, Long memberId) {
        ClubMember clubMember = assertMemberByClubIdAndMemberId(clubId, memberId);
        Club club = clubMember.getClub();
        Member member = clubMember.getMember();
        Article article = assertArticleByArticleIdAndClubId(articleId, club.getId());

        Long likeCount = likeQueryPort.getLikeCount(LikeType.ARTICLE, article.getId());
        List<Member> likedMembers = isLikedMembers(article.getId());

        List<RootCommentResult> commentList = commentQueryPort.getCommentList(article.getId(), member.getId());

        // Todo: isOwner, isLiked, Comment Count 필요
        return ArticleDetailResult.create(article, likeCount, likedMembers, commentList);
    }

    /* ==== Private Helper ==== */
    private ClubMember assertMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB)); // Todo: ErrorCode 수정 필요
    }

    private Article assertArticleByArticleIdAndClubId(Long articleId, Long clubId) {
        return articleReadPort.findByIdAndClubId(articleId, clubId)
                .orElseThrow(() -> new BusinessException(ArticleBusinessError.CLUB_MISMATCH));
    }

    private List<Member> isLikedMembers(Long articleId) {
        List<Long> memberIds = likeQueryPort.getLikedMemberIds(LikeType.ARTICLE, articleId);

        if (memberIds.isEmpty()) return Collections.emptyList();

        List<Member> existingMembers = memberReadPort.findAllByIdInAndIsDeletedFalse(memberIds);

        Map<Long, Member> memberMap = existingMembers.stream()
                .collect(Collectors.toMap(Member::getId, member -> member));

        return memberIds.stream()
                .map(memberMap::get)
                .toList();
    }
}
