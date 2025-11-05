package com.mobble.mobbleserver.application.comment.service;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.command.request.CreateReplyCommentCommand;
import com.mobble.mobbleserver.application.comment.command.request.CreateRootCommentCommand;
import com.mobble.mobbleserver.application.comment.command.request.UpdateCommentCommand;
import com.mobble.mobbleserver.application.comment.error.CommentBusinessError;
import com.mobble.mobbleserver.application.comment.port.provided.CommentCreatePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentDeletePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentUpdatePort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentWritePort;
import com.mobble.mobbleserver.application.common.exception.BusinessException;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.comment.CommentBody;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentModifyService implements CommentCreatePort, CommentUpdatePort, CommentDeletePort {

    private final CommentWritePort commentWritePort;

    private final CommentReadPort commentReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final ArticleReadPort articleReadPort;

    @Override
    public Comment createRootComment(CreateRootCommentCommand command) {
        Member member = assertMemberByClubIdAndMemberId(command.memberId(), command.clubId());
        Article article = assertArticleByArticleIdAndClubId(command.articleId(), command.clubId());

        CommentBody commentBody = CommentBody.of(command.content());
        Comment comment = Comment.createRootComment(member, article, commentBody);

        return commentWritePort.save(comment);
    }

    @Override
    public Comment createReplyComment(CreateReplyCommentCommand command) {
        Member member = assertMemberByClubIdAndMemberId(command.memberId(), command.clubId());
        Article article = assertArticleByArticleIdAndClubId(command.articleId(), command.clubId());
        Comment parentComment = assertCommentByCommentId(command.parentId());

        assertCommentByArticleId(parentComment, article.getId());

        CommentBody commentBody = CommentBody.of(command.content());
        Comment comment = Comment.createReplyComment(member, parentComment, commentBody);

        return commentWritePort.save(comment);
    }

    @Override
    public Comment updateComment(UpdateCommentCommand command) {
        Member member = assertMemberByClubIdAndMemberId(command.memberId(), command.clubId());
        Article article = assertArticleByArticleIdAndClubId(command.articleId(), command.clubId());
        Comment comment = assertCommentByCommentIdAndMemberId(command.commentId(), member.getId());

        assertCommentByArticleId(comment, article.getId());

        CommentBody commentBody = CommentBody.of(command.content());

        return comment.updateContent(commentBody);
    }

    @Override
    public void deleteComment(Long memberId, Long clubId, Long articleId, Long commentId) {
        Member member = assertMemberByClubIdAndMemberId(memberId, clubId);
        Article article = assertArticleByArticleIdAndClubId(articleId, clubId);

        // Todo: Club 권한 정책 로직 수정 필요
        Comment comment = assertCommentByCommentIdAndMemberId(commentId, member.getId());

//        if (ClubPermissionPolicy.isLeaderOrManager(clubMember)) {
//            comment = commentReadPort.findById(commentId).orElseThrow();
//        } else {
//            comment = commentReadPort.findByIdAndMemberId(commentId, memberId).orElseThrow();
//        }

        assertCommentByArticleId(comment, article.getId());

        commentWritePort.delete(comment);
    }

    /* ==== Private Helper ==== */
    private Comment assertCommentByCommentId(Long commentId) {
        return commentReadPort.findById(commentId)
                .orElseThrow(() -> new BusinessException(CommentBusinessError.NOT_FOUND));
    }

    private Comment assertCommentByCommentIdAndMemberId(Long commentId, Long memberId) {
        return commentReadPort.findByIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new BusinessException(CommentBusinessError.NO_PERMISSION));
    }

    private Article assertArticleByArticleIdAndClubId(Long articleId, Long clubId) {
        return articleReadPort.findByIdAndClubId(articleId, clubId)
                .orElseThrow(); // Todo: ErrorCode 수정 필요
    }

    private Member assertMemberByClubIdAndMemberId(Long memberId, Long clubId) {
        ClubMember clubMember = clubMemberReadPort.findClubMemberByClubIdAndMemberId(memberId, clubId)
                .orElseThrow();

        return clubMember.getMember();
    }

    private void assertCommentByArticleId(Comment comment, Long articleId) {
        if (!comment.getArticle().getId().equals(articleId))
            throw new BusinessException(CommentBusinessError.ARTICLE_MISMATCH);
    }
}
