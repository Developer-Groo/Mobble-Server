package com.mobble.mobbleserver.infrastructure.web.article.dto.request;

import com.mobble.mobbleserver.domain.article.ArticleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ArticleCreateRequestDto(
        @NotBlank(message = "title must not be blank")
        @Size(max = 30, message = "title must be 30 characters or fewer")
        String title,

        @NotNull(message = "article type must not be blank")
        ArticleType articleType,

        @NotBlank(message = "body must not be blank")
        @Size(max = 800, message = "body must be 800 characters or fewer")
        String content
) {
}
