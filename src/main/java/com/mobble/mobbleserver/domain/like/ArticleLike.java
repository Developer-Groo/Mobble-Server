package com.mobble.mobbleserver.domain.like;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLike extends AbstractLike {

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Builder(access = AccessLevel.PRIVATE)
    private ArticleLike(Long memberId, Long articleId) {
        super(memberId);
        requireNonNull(articleId, "articleId must not be null");
        this.articleId = articleId;
    }

    public static ArticleLike createArticleLike(Long memberId, Long articleId) {
        return ArticleLike.builder()
                .memberId(memberId)
                .articleId(articleId)
                .build();
    }
}
