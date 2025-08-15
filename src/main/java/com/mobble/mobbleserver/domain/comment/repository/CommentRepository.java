package com.mobble.mobbleserver.domain.comment.repository;

import com.mobble.mobbleserver.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {

    Optional<Comment> findByIdAndMemberId(Long commentId, Long memberId);

    @Modifying
    void deleteAllCommentByArticle_IdIn(List<Long> articleIds);
}
