package com.mobble.mobbleserver.application.liked.core.service.core;

import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeReadPort;
import com.mobble.mobbleserver.application.liked.core.port.required.LikeWritePort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.like.core.CommentLike;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeModifyService {

    @Qualifier("commentLikePersistenceAdapter")
    private final LikeReadPort<CommentLike> likeReadPort;

    @Qualifier("commentLikePersistenceAdapter")
    private final LikeWritePort<CommentLike> likeWritePort;

    private final MemberReadPort memberReadPort;
    private final CommentReadPort commentReadPort;
    private final ClubMemberReadPort clubMemberReadPort;

    public void toggleLike(Long commentId, Long memberId) {
        Comment comment = findCommentByIdOrThrow(commentId);
        Member member = findMemberByMemberIdOrThrow(memberId);

        Long clubId = comment.getArticle().getClub().getId();
        validateClubMember(clubId, member.getId());

        Optional<CommentLike> existLike = likeReadPort.findLike(commentId, memberId);

        if (existLike.isPresent()) {
            likeWritePort.delete(existLike.get());
        } else {
            likeWritePort.save(CommentLike.createCommentLike(member.getId(), comment.getId()));
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

    private void validateClubMember(Long clubId, Long memberId) {
            clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new DomainException(ClubMemberErrorCode.NOT_JOINED_CLUB));
    }
}
