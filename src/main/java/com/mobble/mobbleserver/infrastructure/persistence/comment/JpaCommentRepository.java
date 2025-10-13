package com.mobble.mobbleserver.infrastructure.persistence.comment;

import com.mobble.mobbleserver.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaCommentRepository extends JpaRepository<Comment, Long>, CommentQueryDslRepository {

    Optional<Comment> findByIdAndMemberId(Long commentId, Long memberId);

    void deleteAllCommentByArticle_IdIn(List<Long> articleIds);
}
