package com.mobble.mobbleserver.refactor.like.articleLike.service;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import com.mobble.mobbleserver.refactor.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.refactor.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.refactor.like.baseLike.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleLikeQueryServiceTest {

    @Mock
    private ArticleValidator articleValidator;

    @Mock
    private ArticleLikeRepository articleLikeRepository;

    @InjectMocks
    ArticleLikeQueryService articleLikeQueryService;

    private static final Long ARTICLE_ID = 1L;

    private Article mockArticle;

    @BeforeEach
    void setUp() {
        mockArticle = mock(Article.class);
    }

    @Test
    @DisplayName("게시글 좋아요 한 멤버 목록 조회 성공")
    void success_get_liked_member_list() {
        // given
        ArticleLike mockLike1 = mock(ArticleLike.class);
        ArticleLike mockLike2 = mock(ArticleLike.class);
        Member mockMember1 = mock(Member.class);
        Member mockMember2 = mock(Member.class);

        given(mockLike1.getMember()).willReturn(mockMember1);
        given(mockLike2.getMember()).willReturn(mockMember2);

        given(mockArticle.getId()).willReturn(ARTICLE_ID);
        given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);
        given(articleLikeRepository.findAllByArticleId(ARTICLE_ID)).willReturn(List.of(mockLike1, mockLike2));

        // when
        LikeMemberListResponseDto response = articleLikeQueryService.getLikedMemberList(ARTICLE_ID);

        // then
        assertThat(response.targetId()).isEqualTo(ARTICLE_ID);
        assertThat(response.likedMembers()).hasSize(2);
        verify(articleValidator).findArticleByArticleIdOrThrow(ARTICLE_ID);
        verify(articleLikeRepository).findAllByArticleId(ARTICLE_ID);
    }

    @Test
    @DisplayName("조회할 게시글이 존재하지 않으면 예외 발생")
    void fail_when_article_not_found() {
        // given
        given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willThrow(new DomainException(ArticleErrorCode.NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> articleLikeQueryService.getLikedMemberList(ARTICLE_ID))
                .isInstanceOf(DomainException.class)
                .hasMessage(ArticleErrorCode.NOT_FOUND.message());

        verify(articleValidator).findArticleByArticleIdOrThrow(ARTICLE_ID);
    }

    @Test
    @DisplayName("좋아요한 멤버가 없으면 빈 리스트 반환")
    void success_when_no_liked_members() {
        // given
        given(mockArticle.getId()).willReturn(ARTICLE_ID);
        given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);
        given(articleLikeRepository.findAllByArticleId(ARTICLE_ID)).willReturn(List.of());

        // when
        LikeMemberListResponseDto response = articleLikeQueryService.getLikedMemberList(ARTICLE_ID);

        // then
        assertThat(response.targetId()).isEqualTo(ARTICLE_ID);
        assertThat(response.likedMembers()).isEmpty();

        verify(articleValidator).findArticleByArticleIdOrThrow(ARTICLE_ID);
        verify(articleLikeRepository).findAllByArticleId(ARTICLE_ID);
    }
}
