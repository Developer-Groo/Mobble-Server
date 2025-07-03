package com.mobble.mobbleserver.domain.articleLike.repository;

import com.mobble.mobbleserver.domain.articleLike.entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

}