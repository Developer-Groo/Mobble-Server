package com.mobble.mobbleserver.refactor.like.articleLike.entity;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.like.baseLike.entity.BaseLike;
import com.mobble.mobbleserver.refactor.member.entity.Member;
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
        if (article == null) throw new DomainException(LikeErrorCode.ARTICLE_REQUIRED);
        this.article = article;
        assignMember(member);
    }

    public static ArticleLike createArticleLike(Article article, Member member) {
        return ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
    }
}
