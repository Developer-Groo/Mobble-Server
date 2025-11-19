package com.mobble.mobbleserver.application.comment.service;

import com.mobble.mobbleserver.application.article.error.ArticleBusinessError;
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.command.CreateReplyCommentCommand;
import com.mobble.mobbleserver.application.comment.command.CreateRootCommentCommand;
import com.mobble.mobbleserver.application.comment.command.UpdateCommentCommand;
import com.mobble.mobbleserver.application.comment.error.CommentBusinessError;
import com.mobble.mobbleserver.application.comment.port.provided.CommentCreatePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentDeletePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentUpdatePort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentWritePort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.like.port.provided.LikeModifyPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.comment.CommentContent;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentModifyService implements CommentCreatePort, CommentUpdatePort, CommentDeletePort {

    private final CommentWritePort commentWritePort;
    private final LikeModifyPort likeModifyPort;

    private final CommentReadPort commentReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final ArticleReadPort articleReadPort;

    @Override
    public Comment createRootComment(CreateRootCommentCommand command) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(command.clubId(), command.memberId());
        Member member = clubMember.getMember();
        Article article = assertArticleByArticleIdAndClubId(command.articleId(), command.clubId());

        CommentContent commentContent = CommentContent.of(command.content());
        Comment comment = Comment.createRootComment(member, article, commentContent);

        return commentWritePort.save(comment);
    }

    @Override
    public Comment createReplyComment(CreateReplyCommentCommand command) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(command.clubId(), command.memberId());
        Member member = clubMember.getMember();
        Article article = assertArticleByArticleIdAndClubId(command.articleId(), command.clubId());
        Comment parentComment = assertCommentByCommentIdAndArticleId(command.parentId(), article.getId());

        CommentContent content = CommentContent.of(command.content());
        Comment comment = Comment.createReplyComment(member, parentComment, content);

        return commentWritePort.save(comment);
    }

    @Override
    public Comment update(UpdateCommentCommand command) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(command.clubId(), command.memberId());
        Member member = clubMember.getMember();
        Article article = assertArticleByArticleIdAndClubId(command.articleId(), command.clubId());
        Comment comment = assertCommentByCommentIdAndArticleId(command.commentId(), article.getId());

        assertCanUpdateComment(member, comment);

        CommentContent commentContent = CommentContent.of(command.content());

        return comment.updateContent(commentContent);
    }

    @Override
    public void delete(Long memberId, Long clubId, Long articleId, Long commentId) {
        ClubMember clubMember = assertClubMemberByClubIdAndMemberId(clubId, memberId);
        Article article = assertArticleByArticleIdAndClubId(articleId, clubId);
        Comment comment = assertCommentByCommentIdAndArticleId(commentId, article.getId());

        assertCanDeleteComment(clubMember, comment);

        likeModifyPort.delete(LikeType.COMMENT, comment.getId());
        commentWritePort.delete(comment);
    }

    @Override
    public void deleteAll(Long clubId, Long articleId) {
        Article article = assertArticleByArticleIdAndClubId(articleId, clubId);
        List<Long> commentIds = commentReadPort.findIdsByArticleId(article.getId());

        if (commentIds.isEmpty()) return;

        likeModifyPort.deleteAll(LikeType.COMMENT, commentIds);
        commentWritePort.deleteAllByArticleId(article.getId());
    }

    @Override
    public void deleteAll(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) return;

        List<Long> commentIds = commentReadPort.findIdsByArticleIdIn(articleIds);

        if (commentIds.isEmpty()) return;

        likeModifyPort.deleteAll(LikeType.COMMENT, commentIds);
        commentWritePort.deleteAllByArticleIdIn(articleIds);
    }

    /* ==== Private Helper ==== */
    private Comment assertCommentByCommentIdAndArticleId(Long commentId, Long articleId) {
        return commentReadPort.findByIdAndArticleId(commentId, articleId)
                .orElseThrow(() -> new BusinessException(CommentBusinessError.ARTICLE_MISMATCH));
    }

    private Article assertArticleByArticleIdAndClubId(Long articleId, Long clubId) {
        return articleReadPort.findByIdAndClubId(articleId, clubId)
                .orElseThrow(() -> new BusinessException(ArticleBusinessError.CLUB_MISMATCH));
    }

    private ClubMember assertClubMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB)); // Todo: ErrorCode 수정 필요
    }

    private void assertCanUpdateComment(Member member, Comment comment) {
        if (!comment.isOwner(member.getId())) throw new BusinessException(CommentBusinessError.NO_PERMISSION);
    }

    private void assertCanDeleteComment(ClubMember clubMember, Comment comment) {
        if (clubMember.canManage()) return;

        if (comment.isOwner(clubMember.getMember().getId())) return;

        throw new BusinessException(CommentBusinessError.NO_PERMISSION);
    }
}
