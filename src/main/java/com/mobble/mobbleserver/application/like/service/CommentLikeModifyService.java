package com.mobble.mobbleserver.application.like.service;

import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.like.required.LikeReadPort;
import com.mobble.mobbleserver.application.like.required.LikeWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.like.commentLike.CommentLike;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.refactor.clubMember.validator.ClubMemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeModifyService {

    private final ClubMemberValidator clubMemberValidator;

    private final CommentReadPort commentReadPort;
    private final MemberReadPort memberReadPort;

    @Qualifier("commentLikePersistenceAdapter")
    private final LikeReadPort<CommentLike> likeReadPort;

    @Qualifier("commentLikePersistenceAdapter")
    private final LikeWritePort<CommentLike> likeWritePort;

    public void toggleLike(Long commentId, Long memberId) {
        Comment comment = findCommentByIdOrThrow(commentId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        Long clubId = comment.getArticle().getClub().getId();
        clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, member.getId());

        Optional<CommentLike> existLike = likeReadPort.findLike(commentId, memberId);

        if (existLike.isPresent()) {
            likeWritePort.delete(existLike.get());
        } else {
            likeWritePort.save(CommentLike.createCommentLike(comment, member));
        }
    }

    private Comment findCommentByIdOrThrow(Long commentId) {
        return commentReadPort.findById(commentId)
                .orElseThrow(() -> new DomainException(CommentErrorCode.NOT_FOUND));
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
