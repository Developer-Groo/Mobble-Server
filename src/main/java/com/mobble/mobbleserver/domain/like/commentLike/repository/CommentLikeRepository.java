package com.mobble.mobbleserver.domain.like.commentLike.repository;

import com.mobble.mobbleserver.domain.like.commentLike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeQueryRepository {

    Optional<CommentLike> findLikedByCommentIdAndMemberId(Long commentId, Long memberId);

    int countCommentLikesByCommentId(Long commentId);

    boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);

    @Modifying
    void deleteAllCommentLikeByComment_Article_IdIn(List<Long> articleIds);
}
