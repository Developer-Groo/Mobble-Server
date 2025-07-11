package com.mobble.mobbleserver.domain.article.dto.request;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ArticleRequestDto(


        @NotBlank(message = "ARTICLE:TITLE_NOT_BLANK")
        @Size(max = 30, message = "ARTICLE:TITLE_TOO_LONG")
        String title,
        @NotBlank(message = "ARTICLE:ARTICLETYPE_NOT_BLANK")
        ArticleType articleType,

        @NotBlank(message = "ARTICLE:CONTENT_NOT_BLANK")
        @Size(max = 800, message = "ARTICLE:CONTENT_TOO_LONG")
        String content
) {

    public Article toEntity(ClubMember clubMember, ArticleType articleType, String title){
        return Article.createArticle(
                clubMember,
                articleType,
                title,
                this.content);
    }
}
