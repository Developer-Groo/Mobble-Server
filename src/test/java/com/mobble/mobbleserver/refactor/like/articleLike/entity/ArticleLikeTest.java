package com.mobble.mobbleserver.refactor.like.articleLike.entity;

import com.mobble.mobbleserver.domain.like.core.ArticleLike;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.support.fixture.article.ArticleTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ArticleLikeTest {

    private final Member mockMember = MemberTestFixture.createDefaultMember();
    private final Article mockArticle = ArticleTestFixture.createDefaultArticle();


    @Test
    @DisplayName("ArticleLike 생성 성공")
    void success_when_create_article_like() {
        // when
        ArticleLike like = ArticleLike.createArticleLike(mockMember.getId(), mockArticle.getId());

        //then
        assertThat(like.getArticleId()).isEqualTo(mockArticle.getId());
        assertThat(like.getMemberId()).isEqualTo(mockMember.getId());
    }

    @Test
    @DisplayName("article 이 null 인 경우 예외 발생")
    void fails_when_article_is_null() {
        // when & then
        assertThatThrownBy(() -> ArticleLike.createArticleLike(mockMember.getId(), null))
                .isInstanceOf(DomainException.class)
                .hasMessage(LikeErrorCode.ARTICLE_REQUIRED.message());
    }

    @Test
    @DisplayName("member 가 null 인 경우 예외 발생")
    void fails_when_member_is_null() {
        // when & then
        assertThatThrownBy(() -> ArticleLike.createArticleLike(null, mockArticle.getId()))
                .isInstanceOf(DomainException.class)
                .hasMessage(LikeErrorCode.MEMBER_REQUIRED.message());
    }
}
