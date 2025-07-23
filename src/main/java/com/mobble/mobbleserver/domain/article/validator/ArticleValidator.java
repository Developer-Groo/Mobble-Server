package com.mobble.mobbleserver.domain.article.validator;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleValidator {

    private final ArticleRepository articleRepository;

    public Article findArticleByArticleIdOrThrow(Long ArticleId) {
        return articleRepository.findById(ArticleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));
    }
}
