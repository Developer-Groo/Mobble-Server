package com.mobble.mobbleserver.domain.comment;

import com.mobble.mobbleserver.domain.comment.error.CommentError;
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
public class CommentBody {

    @Column(name = "content", nullable = false, length = 150)
    String content;

    private CommentBody(String content) {
        requireNonNull(content, "content must not be null");

        if (content.isEmpty()) throw new DomainException(CommentError.EMPTY_CONTENT);
        if (content.length() > 150) throw new DomainException(CommentError.CONTENT_TOO_LONG);

        this.content = content;
    }

    public static CommentBody of(String content) {
        return new CommentBody(content);
    }
}
