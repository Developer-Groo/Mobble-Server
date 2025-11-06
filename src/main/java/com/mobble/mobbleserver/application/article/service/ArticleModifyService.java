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
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.refactor.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.refactor.like.commentLike.repository.CommentLikeRepository;
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

    private final CommentLikeRepository commentLikeRepository;
    private final ArticleLikeRepository articleLikeRepository;

    @Override
    public Article createArticle(CreateArticleCommand command) {
        ClubMember clubMember = assertMemberByMemberIdAndClubId(command.memberId(), command.clubId());

        assertCanPost(clubMember, command.type());

        ArticleContent content = ArticleContent.of(command.title(), command.content());
        Article article = Article.createArticle(clubMember.getClub(), clubMember.getMember(), command.type(), content);

        return articleWritePort.save(article);
    }

    @Override
    public Article updateArticle(UpdateArticleCommand command) {
        assertMemberByMemberIdAndClubId(command.memberId(), command.clubId());
        Article article = assertArticleByArticleIdAndMemberId(command.articleId(), command.memberId());

        ArticleContent content = ArticleContent.of(command.title(), command.content());

        return article.updateArticle(content);
    }

    @Override
    public void deleteArticle(Long clubId, Long articleId, Long memberId) {
        ClubMember clubMember = assertMemberByMemberIdAndClubId(memberId, clubId);
        Article article = assertArticleByArticleId(articleId);

        // Todo: Club 권한 정책 로직 수정 필요
//        if (!isOwner && clubMember.getClubMemberRole() == ClubMemberRole.MEMBER) {
//            throw new DomainException(ArticleErrorCode.NO_PERMISSION);
//        }

        // Todo: 댓글 삭제 시 댓글의 좋아요는 댓글 도메인에서 지우도록 수정
        List<Comment> comments = commentReadPort.findCommentsWithRepliesByArticleId(articleId);

        commentLikeRepository.deleteAllByArticleId(articleId);
        commentWritePort.deleteAll(comments);
        articleLikeRepository.deleteAllByArticleId(articleId);
        articleWritePort.delete(article);
    }

    /* ==== Private Helper ==== */
    private Article assertArticleByArticleId(Long articleId) {
        return articleReadPort.findById(articleId)
                .orElseThrow(() -> new BusinessException(ArticleBusinessError.NOT_FOUND));
    }

    private Article assertArticleByArticleIdAndMemberId(Long articleId, Long memberId) {
        return articleReadPort.findByIdAndMemberId(articleId, memberId)
                .orElseThrow(() -> new BusinessException(ArticleBusinessError.NO_PERMISSION));
    }

    private ClubMember assertMemberByMemberIdAndClubId(Long memberId, Long clubId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(memberId, clubId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }

    private void assertCanPost(ClubMember clubMember, ArticleType articleType) {
        if (clubMember.canPost(articleType)) throw new BusinessException(ArticleBusinessError.NO_PERMISSION);
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
