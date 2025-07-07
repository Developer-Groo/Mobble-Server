package com.mobble.mobbleserver.domain.like.commentLike.repository;

import com.mobble.mobbleserver.domain.like.commentLike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findLikedByCommentIdAndMemberId(Long commentId, Long memberId);

    int countCommentLikesByCommentId(Long commentId);
}
