package com.mobble.mobbleserver.domain.like;

import com.mobble.mobbleserver.domain.common.exception.DomainException;
import com.mobble.mobbleserver.domain.like.error.LikeError;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLike extends AbstractLike {

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @Builder(access = AccessLevel.PRIVATE)
    private ArticleLike(Long memberId, Long articleId) {
        super(memberId);
        if (articleId == null) throw new DomainException(LikeError.REQUIRED_ARTICLE);
        this.articleId = articleId;
    }

    public static ArticleLike createArticleLike(Long memberId, Long articleId) {
        return ArticleLike.builder()
                .memberId(memberId)
                .articleId(articleId)
                .build();
    }
}
