package com.mobble.mobbleserver.domain.like.articleLike.entity;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.like.entity.BaseLike;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "article_like_id"))
public class ArticleLike extends BaseLike {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Builder(access = AccessLevel.PRIVATE)
    private ArticleLike(Article article, Member member) {
        validateArticle(article);
        this.article = article;
        assignMember(member);
    }

    public static ArticleLike createArticleLike(Article article, Member member) {
        return ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
    }

    private void validateArticle(Article article) {
        if (article == null) throw new DomainException(LikeErrorCode.ARTICLE_REQUIRED);
    }
}
