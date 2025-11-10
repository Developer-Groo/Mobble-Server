package com.mobble.mobbleserver.infrastructure.persistence.comment;

import com.mobble.mobbleserver.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaCommentRepository extends JpaRepository<Comment, Long>, CommentQueryDslRepository {

    Optional<Comment> findByIdAndArticleId(Long commentId, Long articleId);

    List<Long> findIdsByArticleId(Long articleId);

    List<Long> findIdsByArticleIdIn(List<Long> articleIds);

    void deleteAllByArticleId(Long articleId);

    void deleteAllByArticleIdIn(List<Long> articleIds);
}
