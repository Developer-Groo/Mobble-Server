package com.mobble.mobbleserver.infrastructure.web.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(
        @NotBlank(message = "content must not be blank")
        @Size(max = 100, message = "content must be 100 characters or fewer")
        String content
) {
}
