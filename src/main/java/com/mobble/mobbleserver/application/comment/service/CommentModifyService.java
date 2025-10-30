package com.mobble.mobbleserver.application.comment.service;

import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentCreatePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentDeletePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentUpdatePort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentWritePort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.comment.CommentBody;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mobble.mobbleserver.application.comment.command.CommentCommand.*;

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
        Article article = assertArticleById(command.articleId());
        // Todo: clubId api 에서 받기
        Long clubId = article.getClub().getId();
        Member member = assertMemberByClubIdAndMemberId(clubId, command.memberId());

        CommentBody commentBody = CommentBody.of(command.content());
        Comment comment = Comment.createRootComment(member, article, commentBody);

        return commentWritePort.save(comment);
    }

    @Override
    public Comment createReplyComment(CreateReplyCommentCommand command) {
        Article article = assertArticleById(command.articleId());
        Long clubId = article.getClub().getId();
        Member member = assertMemberByClubIdAndMemberId(clubId, command.memberId());

        Comment parentComment = commentReadPort.findById(command.parentId()).orElseThrow();

        assertCommentByArticleId(parentComment, article.getId());

        CommentBody commentBody = CommentBody.of(command.content());
        Comment comment = Comment.createReplyComment(member, parentComment, commentBody);

        return commentWritePort.save(comment);
    }

    @Override
    public Comment updateComment(UpdateCommentCommand command) {
        Article article = assertArticleById(command.articleId());
        Club club = article.getClub();
        Member member = assertMemberByClubIdAndMemberId(club.getId(), command.memberId());
        Comment comment = commentReadPort.findByIdAndMemberId(command.commentId(), member.getId()).orElseThrow();

        assertCommentByArticleId(comment, article.getId());

        CommentBody commentBody = CommentBody.of(command.content());

        return comment.updateContent(commentBody);
    }

    @Override
    public void deleteComment(Long articleId, Long commentId, Long memberId) {
        Article article = assertArticleById(articleId);
        Club club = article.getClub();
        Member member = assertMemberByClubIdAndMemberId(club.getId(), memberId);

        Comment comment = commentReadPort.findByIdAndMemberId(commentId, member.getId()).orElseThrow();

        // Todo: Club 권한 정책 로직 수정 필요
//        if (ClubPermissionPolicy.isLeaderOrManager(clubMember)) {
//            comment = commentReadPort.findById(commentId).orElseThrow();
//        } else {
//            comment = commentReadPort.findByIdAndMemberId(commentId, memberId).orElseThrow();
//        }

        assertCommentByArticleId(comment, article.getId());

        commentWritePort.delete(comment);
    }

    private void assertCommentByArticleId(Comment comment, Long articleId) {
        if (!comment.getArticle().getId().equals(articleId))
            throw new DomainException(CommentErrorCode.ARTICLE_REQUIRED);
    }

    private Member assertMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        ClubMember clubMember = clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));

        return clubMember.getMember();
    }

    private Article assertArticleById(Long articleId) {
        return articleReadPort.findById(articleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));
    }
}
