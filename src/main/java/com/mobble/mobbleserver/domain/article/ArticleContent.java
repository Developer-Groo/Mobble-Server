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

    @Column(name = "content", nullable = false, length = 800)
    String content;

    private ArticleContent(String title, String content) {
        requireNonNull(title, "title must not be null");
        requireNonNull(content, "content must not be null");

        if (title.isEmpty() || content.isEmpty()) throw new DomainException(ArticleError.EMPTY_CONTENT);
        if (title.length() > 50 || content.length() > 800) throw new DomainException(ArticleError.CONTENT_TOO_LONG);

        this.title = title;
        this.content = content;
    }

    public static ArticleContent of(String title, String content) {
        return new ArticleContent(title, content);
    }
}
