package com.mobble.mobbleserver.domain.like.articleLike.entity;

import com.mobble.mobbleserver.domain.article.entity.Article;
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
public class ArticleLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private ArticleLike(Article article, Member member) {
        this.article = article;
        this.member = member;
    }

    public static ArticleLike createArticleLike(Article article, Member member) {
        if (article == null) throw new DomainException(LikeErrorCode.ARTICLE_REQUIRED);
        if (member == null) throw new DomainException(LikeErrorCode.MEMBER_REQUIRED);

        return ArticleLike.builder()
                .article(article)
                .member(member)
                .build();
    }
}
