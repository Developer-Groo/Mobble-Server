package com.mobble.mobbleserver.infrastructure.web.article.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ArticleUpdateRequestDto(
        @NotBlank(message = "title must not be blank")
        @Size(max = 30, message = "title must be 30 characters or fewer")
        String title,

        @NotBlank(message = "body must not be blank")
        @Size(max = 800, message = "body must be 800 characters or fewer")
        String content
) {
}
