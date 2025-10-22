package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

import com.mobble.mobbleserver.domain.like.commentLike.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface JpaCommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeQueryDslRepository {

    Optional<CommentLike> findLikedByCommentIdAndMemberId(Long commentId, Long memberId);

    @Modifying
    void deleteAllCommentLikeByComment_Article_IdIn(List<Long> articleIds);
}
