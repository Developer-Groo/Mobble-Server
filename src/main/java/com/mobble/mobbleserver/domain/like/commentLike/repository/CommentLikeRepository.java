package com.mobble.mobbleserver.domain.like.commentLike.repository;

import com.mobble.mobbleserver.domain.like.commentLike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

}
