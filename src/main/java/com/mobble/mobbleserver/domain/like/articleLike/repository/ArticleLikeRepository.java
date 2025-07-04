package com.mobble.mobbleserver.domain.like.articleLike.repository;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

    Optional<ArticleLike> findArticleLikeByArticleAndMember(Article article, Member member);

    int countArticleLikesByArticle(Article article);

    List<ArticleLike> findAllArticleLikedMemberByArticle(Article article);
}