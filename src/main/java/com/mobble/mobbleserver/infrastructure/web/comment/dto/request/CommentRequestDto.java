package com.mobble.mobbleserver.infrastructure.web.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(
        @NotBlank(message = "COMMENT:CONTENT_NOT_BLANK")
        @Size(max = 100, message = "COMMENT:CONTENT_TOO_LONG")
        String content
) {
}
