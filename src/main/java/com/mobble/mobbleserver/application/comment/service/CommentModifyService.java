package com.mobble.mobbleserver.application.comment.service;

import com.mobble.mobbleserver.application.comment.port.provided.CommentCreatePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentDeletePort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentUpdatePort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentWritePort;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentErrorCode;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.club.policy.ClubPermissionPolicy;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentModifyService implements CommentCreatePort, CommentUpdatePort, CommentDeletePort {

    private final ClubMemberValidator clubMemberValidator;
    private final ArticleValidator articleValidator;

    private final CommentWritePort commentWritePort;
    private final CommentReadPort commentReadPort;

    @Override
    public Comment createRootComment(Long memberId, Long articleId, CommentRequestDto dto) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        Member member = clubMember.getMember();
        Comment comment = dto.toEntity(member, article);

        return commentWritePort.save(comment);
    }

    @Override
    public Comment createReplyComment(
            Long memberId,
            Long articleId,
            Long parentCommentId,
            CommentRequestDto dto
    ) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Comment parentComment = commentReadPort.findById(parentCommentId).orElseThrow();

        Member member = clubMember.getMember();
        Comment comment = dto.toEntity(member, article, parentComment);

        return commentWritePort.save(comment);
    }

    @Override
    public Comment updateComment(
            Long articleId,
            Long commentId,
            Long memberId,
            CommentRequestDto dto
    ) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        Club club = article.getClub();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(club.getId(), memberId);
        Member member = clubMember.getMember();
        Comment comment = commentReadPort.findByIdAndMemberId(commentId, member.getId()).orElseThrow();

        validateCommentByArticleIdOrThrow(comment, article.getId());

        return comment.updateContent(dto.content());
    }

    @Override
    public void deleteComment(Long articleId, Long commentId, Long memberId) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        Club club = article.getClub();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(club.getId(), memberId);

        Comment comment;

        if (ClubPermissionPolicy.isLeaderOrManager(clubMember)) {
            comment = commentReadPort.findById(commentId).orElseThrow();
        } else {
            comment = commentReadPort.findByIdAndMemberId(commentId, memberId).orElseThrow();
        }

        validateCommentByArticleIdOrThrow(comment, article.getId());

        commentWritePort.delete(comment);
    }

    private void validateCommentByArticleIdOrThrow(Comment comment, Long articleId) {
        if (!comment.getArticle().getId().equals(articleId)) throw new DomainException(CommentErrorCode.ARTICLE_REQUIRED);
    }
}
