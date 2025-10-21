package com.mobble.mobbleserver.infrastructure.persistence.like.commentLike;

import com.mobble.mobbleserver.application.like.required.LikeReadPort;
import com.mobble.mobbleserver.application.like.required.LikeWritePort;
import com.mobble.mobbleserver.domain.like.commentLike.CommentLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("commentLikePersistenceAdapter")
@RequiredArgsConstructor
public class CommentLikePersistenceAdapter implements LikeWritePort<CommentLike>, LikeReadPort<CommentLike> {

    private final JpaCommentLikeRepository jpaCommentLikeRepository;

    /**
     * LikeWritePort
     */
    @Override
    public CommentLike save(CommentLike commentLike) {
        return jpaCommentLikeRepository.save(commentLike);
    }

    @Override
    public void delete(CommentLike commentLike) {
        jpaCommentLikeRepository.delete(commentLike);
    }

    /**
     * LikeReadPort
     */
    @Override
    public Optional<CommentLike> findLike(Long targetId, Long memberId) {
        return jpaCommentLikeRepository.findLikedByCommentIdAndMemberId(targetId, memberId);
    }
}
