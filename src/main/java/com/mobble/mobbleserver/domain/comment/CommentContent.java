package com.mobble.mobbleserver.domain.comment;

import com.mobble.mobbleserver.domain.comment.error.CommentError;
import com.mobble.mobbleserver.domain.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class CommentContent {

    @Column(name = "body", nullable = false, length = 150)
    String body;

    private CommentContent(String body) {
        requireNonNull(body, "body must not be null");

        if (body.isEmpty()) throw new DomainException(CommentError.EMPTY_CONTENT);
        if (body.length() > 150) throw new DomainException(CommentError.CONTENT_TOO_LONG);

        this.body = body;
    }

    public static CommentContent of(String body) {
        return new CommentContent(body);
    }
}
