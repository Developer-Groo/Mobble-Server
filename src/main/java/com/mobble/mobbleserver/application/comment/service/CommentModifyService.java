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
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentErrorCode;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.refactor.club.policy.ClubPermissionPolicy;
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
    public Comment createRootComment(Long memberId, Long articleId, CommentRequestDto dto) {
        Article article = findArticleByArticleIdOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

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
        Article article = findArticleByArticleIdOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Comment parentComment = commentReadPort.findById(parentCommentId).orElseThrow();

        Member member = clubMember.getMember();
        Comment comment = dto.toEntity(member, parentComment);

        return commentWritePort.save(comment);
    }

    @Override
    public Comment updateComment(
            Long articleId,
            Long commentId,
            Long memberId,
            CommentRequestDto dto
    ) {
        Article article = findArticleByArticleIdOrThrow(articleId);
        Club club = article.getClub();
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(club.getId(), memberId);
        Member member = clubMember.getMember();
        Comment comment = commentReadPort.findByIdAndMemberId(commentId, member.getId()).orElseThrow();

        validateCommentByArticleIdOrThrow(comment, article.getId());

        return comment.updateContent(dto.content());
    }

    @Override
    public void deleteComment(Long articleId, Long commentId, Long memberId) {
        Article article = findArticleByArticleIdOrThrow(articleId);
        Club club = article.getClub();
        ClubMember clubMember = findClubMemberByClubIdAndMemberIdOrThrow(club.getId(), memberId);

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
        if (!comment.getArticle().getId().equals(articleId))
            throw new DomainException(CommentErrorCode.ARTICLE_REQUIRED);
    }

    private ClubMember findClubMemberByClubIdAndMemberIdOrThrow(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }

    private Article findArticleByArticleIdOrThrow(Long articleId) {
        return articleReadPort.findById(articleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));
    }
}
