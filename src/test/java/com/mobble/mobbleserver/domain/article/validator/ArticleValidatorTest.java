package com.mobble.mobbleserver.domain.article.validator;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ArticleValidatorTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleValidator articleValidator;

    @Test
    @DisplayName("게시글 ID로 조회 성공")
    void success_when_find_article_or_throw() {
        // given
        Long articleId = 1L;
        Article mockArticle = mock(Article.class);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(mockArticle));

        // when
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);

        // then
        assertThat(article).isEqualTo(mockArticle);
    }

    @Test
    @DisplayName("게시글 ID로 조회 실패 시 예외 발생")
    void fails_when_find_article_or_throw() {
        // given
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> articleValidator.findArticleByArticleIdOrThrow(articleId))
                .isInstanceOf(DomainException.class)
                .hasMessage(ArticleErrorCode.NOT_FOUND.message());
    }

    @Test
    @DisplayName("게시글 ID와 작성자 ID로 조회 성공")
    void success_when_find_article_by_article_id_and_member_id_or_throw() {
        // given
        Long articleId = 10L;
        Long memberId = 20L;
        Article mockArticle = mock(Article.class);

        given(articleRepository.findArticleByIdAndMemberId(articleId, memberId))
                .willReturn(Optional.of(mockArticle));

        // when
        Article article = articleValidator.findArticleByArticleIdAndMemberIdOrThrow(articleId, memberId);

        // then
        assertThat(article).isEqualTo(mockArticle);
    }
