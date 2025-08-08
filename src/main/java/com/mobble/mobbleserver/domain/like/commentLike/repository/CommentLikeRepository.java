package com.mobble.mobbleserver.domain.like.commentLike.repository;

import com.mobble.mobbleserver.domain.like.commentLike.entity.CommentLike;
import com.mobble.mobbleserver.domain.like.repository.GenericLikeRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends GenericLikeRepository<CommentLike>, CommentLikeQueryRepository {

    Optional<CommentLike> findLikedByCommentIdAndMemberId(Long commentId, Long memberId);

    @Modifying
    void deleteAllCommentLikeByComment_Article_IdIn(List<Long> articleIds);
}
