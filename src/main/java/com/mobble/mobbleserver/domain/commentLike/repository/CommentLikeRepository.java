package com.mobble.mobbleserver.domain.commentLike.repository;

import com.mobble.mobbleserver.domain.commentLike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

}
