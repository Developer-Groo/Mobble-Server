package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

import com.mobble.mobbleserver.domain.like.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeQueryDslRepository {

    Optional<CommentLike> findLikedByCommentIdAndMemberId(Long commentId, Long memberId);

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);

    void deleteByMemberIdAndCommentId(Long memberId, Long commentId);
}
