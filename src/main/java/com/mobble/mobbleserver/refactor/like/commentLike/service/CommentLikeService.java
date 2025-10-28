package com.mobble.mobbleserver.refactor.like.commentLike.service;

import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.refactor.like.baseLike.service.AbstractLikeService;
import com.mobble.mobbleserver.refactor.like.commentLike.entity.CommentLike;
import com.mobble.mobbleserver.refactor.like.commentLike.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentLikeService extends AbstractLikeService<Comment, CommentLike> {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentReadPort commentReadPort;

    private final ClubMemberReadPort clubMemberReadPort;

    @Override
    public LikeType getType() {
        return LikeType.COMMENT;
    }

    @Override
    protected Comment getTarget(Long targetId) {
        return commentReadPort.findById(targetId).orElseThrow();
    }

    @Override
    protected Optional<CommentLike> findExistingLike(Comment comment, Member member) {
        return commentLikeRepository.findLikedByCommentIdAndMemberId(comment.getId(), member.getId());
    }

    @Override
    protected CommentLike createLike(Comment comment, Member member) {
        Long clubId = comment.getArticle().getClub().getId();
        clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, member.getId());

        return CommentLike.createCommentLike(comment, member);
    }

    @Override
    protected void saveLike(CommentLike entity) {
        commentLikeRepository.save(entity);
    }

    @Override
    protected void deleteLike(CommentLike entity) {
        commentLikeRepository.delete(entity);
    }
}
