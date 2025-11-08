package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

import com.mobble.mobbleserver.domain.like.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface JpaCommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeQueryDslRepository {

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);

    @Modifying
    void deleteByMemberIdAndCommentId(Long memberId, Long commentId);

    @Modifying
    void deleteByCommentId(Long commentId);

    @Modifying
    void deleteAllByCommentIdIn(List<Long> commentIds);
}
