package com.mobble.mobbleserver.like.commentLike.repository;

import com.mobble.mobbleserver.like.commentLike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

}
