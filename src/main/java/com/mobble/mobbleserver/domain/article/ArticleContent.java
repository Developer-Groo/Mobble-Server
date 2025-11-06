package com.mobble.mobbleserver.domain.article;

import com.mobble.mobbleserver.domain.article.error.ArticleError;
import com.mobble.mobbleserver.domain.common.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ArticleContent {

    @Column(name = "title", nullable = false, length = 50)
    String title;

    @Column(name = "body", nullable = false, length = 800)
    String body;

    private ArticleContent(String title, String body) {
        requireNonNull(title, "title must not be null");
        requireNonNull(body, "body must not be null");

        if (title.isEmpty() || body.isEmpty()) throw new DomainException(ArticleError.EMPTY_CONTENT);
        if (title.length() > 50 || body.length() > 800) throw new DomainException(ArticleError.CONTENT_TOO_LONG);

        this.title = title;
        this.body = body;
    }

    public static ArticleContent of(String title, String content) {
        return new ArticleContent(title, content);
    }
}
