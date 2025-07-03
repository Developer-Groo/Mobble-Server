package com.mobble.mobbleserver.like.articleLike.repository;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findByArticleAndMember(Article article, Member member);
}