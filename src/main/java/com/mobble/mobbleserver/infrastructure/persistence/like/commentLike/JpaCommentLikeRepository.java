package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

import com.mobble.mobbleserver.domain.like.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeQueryDslRepository {

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);

    void deleteByMemberIdAndCommentId(Long memberId, Long commentId);

    void deleteByCommentId(Long commentId);

    void deleteAllByCommentIdIn(List<Long> commentIds);
}
