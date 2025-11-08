package com.mobble.mobbleserver.application.article.service;

import com.mobble.mobbleserver.application.article.port.provided.ArticleCreatePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleDeletePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleUpdatePort;
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.article.port.required.ArticleWritePort;
import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.command.response.RootCommentResult;
import com.mobble.mobbleserver.application.comment.port.provided.CommentQueryPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentWritePort;
import com.mobble.mobbleserver.application.like.port.required.LikeCounterWritePort;
import com.mobble.mobbleserver.application.like.port.required.LikeWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.ClubMemberRole;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.article.projection.ArticleLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleUpdatedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleModifyService implements ArticleCreatePort, ArticleUpdatePort, ArticleDeletePort {

    private final ArticleWritePort articleWritePort;
    private final CommentWritePort commentWritePort;
    private final LikeWritePort likeWritePort;
    private final LikeCounterWritePort likeCounterWritePort;

    private final ArticleReadPort articleReadPort;
    private final MemberReadPort memberReadPort;
    private final ClubReadPort clubReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final CommentReadPort commentReadPort;

    private final CommentQueryPort commentQueryPort;

    @Override
    public ArticleResponseDto createArticle(Long memberId, Long clubId, ArticleRequestDto dto) {
        Club club = findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(memberId);
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        assertCanPost(clubMember, dto.articleType());
        Article article = dto.toEntity(club, member);

        return ArticleResponseDto.toDto(articleWritePort.save(article));
    }

    @Override
    public ArticleUpdatedResponseDto updateArticle(Long articleId, Long memberId, ArticleRequestDto dto) {
        Article article = findArticleByArticleIdAndMemberIdOrThrow(articleId, memberId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        assertCanPost(clubMember, dto.articleType());
        article.updateArticle(dto.articleType(), dto.title(), dto.content());

        return ArticleUpdatedResponseDto.toDto(article);
    }

    @Override
    public void deleteArticle(Long articleId, Long memberId) {
        Article article = findArticleByArticleIdOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        boolean isOwner = articleReadPort.existsArticleByIdAndMemberId(articleId, memberId);

        if (!isOwner && clubMember.getClubMemberRole() == ClubMemberRole.MEMBER) {
            throw new DomainException(ArticleErrorCode.NO_PERMISSION);
        }

        List<Comment> comments = commentReadPort.findCommentsWithRepliesByArticleId(articleId);

        List<Long> commentIds = comments.stream()
                        .map(Comment::getId)
                                .toList();

        likeWritePort.deleteAllByLikeTypeAndTargetIds(LikeType.COMMENT, commentIds);
        likeCounterWritePort.deleteAllByLikeTypeAndTargetIds(LikeType.COMMENT, commentIds);
        commentWritePort.deleteAll(comments);

        likeWritePort.deleteByLikeTypeAndTargetId(LikeType.COMMENT, articleId);
        likeCounterWritePort.deleteByLikeTypeAndTargetId(LikeType.COMMENT, articleId);
        articleWritePort.delete(article);
    }

    private Article findArticleByArticleIdOrThrow(Long articleId) {
        return articleReadPort.findById(articleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));
    }

    private Article findArticleByArticleIdAndMemberIdOrThrow(Long articleId, Long memberId) {
        return articleReadPort.findByIdAndMemberId(articleId, memberId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND_TO_MEMBER));
    }

    private void assertCanPost(ClubMember clubMember, ArticleType articleType) {
        if (!clubMember.canPost(articleType)) {
            throw new DomainException(ArticleErrorCode.NOTICE_NO_PERMISSION);
        }
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }

    private ClubMember findClubMemberByClubIdAndMemberIdOrThrow(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByMemberIdAndClubId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }

    private ArticleResponseDto convertToArticleResponseDto(Article article, Long memberId) {
        Map<Long, ArticleLikeInfoDto> likeInfoMap = getArticleLikeInfo(List.of(article), memberId);
        ArticleLikeInfoDto likeInfo = likeInfoMap.getOrDefault(article.getId(), new ArticleLikeInfoDto(0, false));
        List<RootCommentResult> comments = commentQueryPort.getCommentListByArticle(article.getId(), memberId);
        int commentCount = comments.size();

        boolean isMine = articleReadPort.existsArticleByIdAndMemberId(article.getId(), memberId);

        return ArticleResponseDto.toDto(article, isMine, likeInfo, commentCount, comments);
    }

    private Map<Long, ArticleLikeInfoDto> getArticleLikeInfo(List<Article> articles, Long memberId) {
        List<Long> articleIds = articles.stream()
                .map(Article::getId)
                .distinct()
                .toList();

        return articleReadPort.findLikeInfoByArticleIdsAndMemberId(articleIds, memberId);
    }
}
