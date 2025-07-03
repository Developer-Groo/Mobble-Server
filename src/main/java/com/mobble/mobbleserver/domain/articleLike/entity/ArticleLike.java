package com.mobble.mobbleserver.domain.articleLike.entity;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.member.entity.Member;
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
    private Article articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @Builder
    private ArticleLike(Article articleId, Member memberId) {
        this.articleId = articleId;
        this.memberId = memberId;
    }

    public static ArticleLike createArticleLike(Article articleId, Member memberId) {
        return ArticleLike.builder()
                .articleId(articleId)
                .memberId(memberId)
                .build();
    }
}
