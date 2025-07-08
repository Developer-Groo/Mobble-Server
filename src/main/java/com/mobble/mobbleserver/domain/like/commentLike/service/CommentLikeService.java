package com.mobble.mobbleserver.domain.like.commentLike.service;

import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.validator.CommentValidator;
import com.mobble.mobbleserver.domain.like.commentLike.dto.response.CommentLikeResponseDto;
import com.mobble.mobbleserver.domain.like.commentLike.entity.CommentLike;
import com.mobble.mobbleserver.domain.like.commentLike.repository.CommentLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeService {

    private final MemberRepository memberRepository;
    private final CommentValidator commentValidator;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public CommentLikeResponseDto toggleLike(@Positive Long commentId, Long memberId) {
        Member member = findMemberOrThrow(memberId);
        Comment comment = commentValidator.findCommentOrThrow(commentId);
//        ClubMember clubMember = findClubMemberOrThrow(clubMemberId);

        Optional<CommentLike> checkLiked = commentLikeRepository.findLikedByCommentIdAndMemberId(comment.getId(), member.getId());

        if (checkLiked.isPresent()) {
            commentLikeRepository.delete(checkLiked.get());
        } else {
            CommentLike commentLike = CommentLike.createcommentLike(comment, member);
            commentLikeRepository.save(commentLike);
        }

        boolean isLiked = !checkLiked.isPresent();
        int likeCount = commentLikeRepository.countCommentLikesByCommentId(comment.getId());

        return CommentLikeResponseDto.toDto(comment.getId(), isLiked, likeCount);
    }

    public CommentLikeResponseDto getCommentLikeCount(Long commentId, Long memberId) {
        Comment comment = commentValidator.findCommentOrThrow(commentId);
        boolean isLiked = (memberId != null) && commentLikeRepository.existsByCommentIdAndMemberId(comment.getId(), memberId);
        int likeCount = commentLikeRepository.countCommentLikesByCommentId(comment.getId());

        return CommentLikeResponseDto.toDto(comment.getId(), isLiked, likeCount);
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }

    private Comment findCommentOrThorw(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }

//    private ClubMember findClubMemberOrThrow(Long clubMemberId) {
//        return clubMemberRepository.findById(clubMemberId)
//                .orElseThrow(() -> new IllegalArgumentException(""));
//    }
}
