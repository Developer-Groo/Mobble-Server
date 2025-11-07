package com.mobble.mobbleserver.application.article.service;

import com.mobble.mobbleserver.application.article.command.request.CreateArticleCommand;
import com.mobble.mobbleserver.application.article.command.request.UpdateArticleCommand;
import com.mobble.mobbleserver.application.article.error.ArticleBusinessError;
import com.mobble.mobbleserver.application.article.port.provided.ArticleCreatePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleDeletePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleUpdatePort;
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.article.port.required.ArticleWritePort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentWritePort;
import com.mobble.mobbleserver.application.common.exception.BusinessException;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleContent;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.like.articleLike.JpaArticleLikeRepository;
import com.mobble.mobbleserver.infrastructure.persistence.like.commentLike.JpaCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleModifyService implements ArticleCreatePort, ArticleUpdatePort, ArticleDeletePort {

    private final ArticleWritePort articleWritePort;
    private final CommentWritePort commentWritePort;

    private final ArticleReadPort articleReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final CommentReadPort commentReadPort;

    private final JpaArticleLikeRepository articleLikeRepository;

    @Override
    public Article createArticle(CreateArticleCommand command) {
        ClubMember clubMember = assertMemberByClubIdAndMemberId(command.clubId(), command.memberId());

        assertCanPost(clubMember, command.type());

        ArticleContent content = ArticleContent.of(command.title(), command.content());
        Article article = Article.createArticle(clubMember.getClub(), clubMember.getMember(), command.type(), content);

        return articleWritePort.save(article);
    }

    @Override
    public Article updateArticle(UpdateArticleCommand command) {
        ClubMember clubMember = assertMemberByClubIdAndMemberId(command.clubId(), command.memberId());
        Club club = clubMember.getClub();
        Member member = clubMember.getMember();
        Article article = assertArticleByArticleIdAndClubId(command.articleId(), club.getId());

        assertCanUpdateArticle(member, article);

        ArticleContent content = ArticleContent.of(command.title(), command.content());

        return article.updateArticle(content);
    }

    @Override
    public void deleteArticle(Long clubId, Long articleId, Long memberId) {
        ClubMember clubMember = assertMemberByClubIdAndMemberId(clubId, memberId);
        Article article = assertArticleByArticleIdAndClubId(articleId, clubId);

        assertCanDeleteArticle(article, clubMember);

        // Todo: 댓글 삭제 시 댓글의 좋아요는 댓글 도메인에서 지우도록 수정
        List<Comment> comments = commentReadPort.findCommentsWithRepliesByArticleId(articleId);

        commentWritePort.deleteAll(comments);

        articleLikeRepository.deleteAllByArticleId(articleId);
        articleWritePort.delete(article);
    }

    /* ==== Private Helper ==== */
    private Article assertArticleByArticleIdAndClubId(Long articleId, Long clubId) {
        return articleReadPort.findByIdAndClubId(articleId, clubId)
                .orElseThrow(() -> new BusinessException(ArticleBusinessError.CLUB_MISMATCH));
    }

    private ClubMember assertMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB)); // Todo: ErrorCode 수정 필요
    }

    private void assertCanPost(ClubMember clubMember, ArticleType articleType) {
        if (!clubMember.canPost(articleType)) throw new BusinessException(ArticleBusinessError.NO_PERMISSION);
    }

    private void assertCanUpdateArticle(Member member, Article article) {
        if (!article.isOwner(member.getId())) throw new BusinessException(ArticleBusinessError.NO_PERMISSION);
    }

    private void assertCanDeleteArticle(Article article, ClubMember clubMember) {
        if (clubMember.canManage()) return;

        if (article.isOwner(clubMember.getMember().getId())) return;

        throw new BusinessException(ArticleBusinessError.NO_PERMISSION);
    }

//    private ArticleResponseDto convertToArticleResponseDto(Article article, Long memberId) {
//        Map<Long, ArticleLikeInfoDto> likeInfoMap = getArticleLikeInfo(List.of(article), memberId);
//        ArticleLikeInfoDto likeInfo = likeInfoMap.getOrDefault(article.getId(), new ArticleLikeInfoDto(0, false));
//        List<RootCommentResult> comments = commentQueryPort.getCommentListByArticle(article.getId(), memberId);
//        int commentCount = comments.size();
//
//        boolean isMine = articleReadPort.existsArticleByIdAndMemberId(article.getId(), memberId);
//
//        return ArticleResponseDto.toDto(article, isMine, likeInfo, commentCount, comments);
//    }

//    private Map<Long, ArticleLikeInfoDto> getArticleLikeInfo(List<Article> articles, Long memberId) {
//        List<Long> articleIds = articles.stream()
//                .map(Article::getId)
//                .distinct()
//                .toList();
//
//        return articleReadPort.findLikeInfoByArticleIdsAndMemberId(articleIds, memberId);
//    }
}
