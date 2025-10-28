package com.mobble.mobbleserver.domain.like.core;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
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
        if (articleId == null) throw new DomainException(LikeErrorCode.ARTICLE_REQUIRED);
        this.articleId = articleId;
    }

    public static ArticleLike create(Long memberId, Long articleId) {
        return ArticleLike.builder()
                .memberId(memberId)
                .articleId(articleId)
                .build();
    }
}
