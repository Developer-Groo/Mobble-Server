package com.mobble.mobbleserver.domain.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.isTrue;

@Value
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class CommentBody {

    @Column(name = "content", nullable = false, length = 150)
    String content;

    private CommentBody(String content) {
        requireNonNull(content, "content must not be null");
        hasText(content, "content must not be empty");
        isTrue(content.length() <= 150, "content length must be less than or equal to 150");

        this.content = content;
    }

    public static CommentBody of(String content) {
        return new CommentBody(content);
    }
}
