package com.mobble.mobbleserver.refactor.like.commentLike.repository;

import com.mobble.mobbleserver.domain.like.commentLike.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends CommentLikeQueryRepository, JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findLikedByCommentIdAndMemberId(Long commentId, Long memberId);

    @Modifying
    void deleteAllCommentLikeByComment_Article_IdIn(List<Long> articleIds);
}
