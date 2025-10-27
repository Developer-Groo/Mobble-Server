package com.mobble.mobbleserver.infrastructure.web.article.dto.request;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ArticleRequestDto(
        @NotBlank(message = "ARTICLE:TITLE_NOT_BLANK")
        @Size(max = 30, message = "ARTICLE:TITLE_TOO_LONG")
        String title,

        @NotNull(message = "ARTICLE:ARTICLETYPE_NOT_NULL")
        ArticleType articleType,

        @NotBlank(message = "ARTICLE:CONTENT_NOT_BLANK")
        @Size(max = 800, message = "ARTICLE:CONTENT_TOO_LONG")
        String content
) {

    public Article toEntity(Club club, Member member) {
        return Article.createArticle(
                club,
                member,
                this.articleType,
                this.title,
                this.content
        );
    }
}
