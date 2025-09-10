package com.mobble.mobbleserver.domain.like.articleLike.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberValidationErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ArticleLikeServiceTest {

    @Mock
    private ArticleLikeRepository articleLikeRepository;

    @Mock
    private ArticleValidator articleValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @InjectMocks
    private ArticleLikeService articleLikeService;

    private static final Long ARTICLE_ID = 1L;
    private static final Long MEMBER_ID = 2L;

    private Article mockArticle;
    private Member mockMember;
    private Club mockClub;
    private ClubMember mockClubMember;
    private ArticleLike mockArticleLike;

    @BeforeEach
    void setUp() {
        mockArticle = mock(Article.class);
        mockMember = mock(Member.class);
        mockClub = mock(Club.class);
        mockClubMember = mock(ClubMember.class);
        mockArticleLike = mock(ArticleLike.class);
    }

    @DisplayName("좋아요가 없으면 생성")
    @Test
    void success_when_not_liked() {
        // given
        Long clubId = 10L;

        given(mockArticle.getId()).willReturn(ARTICLE_ID);
        given(mockMember.getId()).willReturn(MEMBER_ID);
        given(mockArticle.getClub()).willReturn(mockClub);
        given(mockClub.getId()).willReturn(clubId);

        given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, MEMBER_ID)).willReturn(mockClubMember);
        given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);
        given(articleLikeRepository.findLikedByArticleIdAndMemberId(ARTICLE_ID, MEMBER_ID)).willReturn(Optional.empty());

        // when
        boolean result = articleLikeService.toggleLike(ARTICLE_ID, mockMember).isLiked();

        // then
        assertThat(result).isTrue();
        verify(articleLikeRepository).save(any(ArticleLike.class));
    }

    @DisplayName("좋아요가 있으면 삭제")
    @Test
    void success_when_has_liked() {
        // given
        given(mockArticle.getId()).willReturn(ARTICLE_ID);
        given(mockMember.getId()).willReturn(MEMBER_ID);

        given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);
        given(articleLikeRepository.findLikedByArticleIdAndMemberId(ARTICLE_ID, MEMBER_ID)).willReturn(Optional.of(mockArticleLike));

        // when
        boolean result = articleLikeService.toggleLike(ARTICLE_ID, mockMember).isLiked();

        // then
        assertThat(result).isFalse();
        verify(articleLikeRepository).delete(mockArticleLike);
        verify(articleLikeRepository, never()).save(any());
    }

    @DisplayName("좋아요할 게시글이 없으면 예외 발생")
    @Test
    void fail_when_article_not_found() {
        // given
        given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willThrow(new DomainException(LikeErrorCode.ARTICLE_REQUIRED));

        //when & then
        assertThatThrownBy(() -> articleLikeService.toggleLike(ARTICLE_ID, mockMember))
                .isInstanceOf(DomainException.class)
                .hasMessage(LikeErrorCode.ARTICLE_REQUIRED.message());
    }

    @DisplayName("클럽 멤버가 아니면 예외 발생")
    @Test
    void fail_when_not_club_member() {
        // given
        Long clubId = 10L;

        given(mockArticle.getId()).willReturn(ARTICLE_ID);
        given(mockMember.getId()).willReturn(MEMBER_ID);
        given(mockArticle.getClub()).willReturn(mockClub);
        given(mockClub.getId()).willReturn(clubId);

        given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID)).willReturn(mockArticle);
        given(articleLikeRepository.findLikedByArticleIdAndMemberId(ARTICLE_ID, MEMBER_ID)).willReturn(Optional.empty());
        given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, MEMBER_ID)).willThrow(new DomainException(ClubMemberValidationErrorCode.CLUB_MEMBER_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> articleLikeService.toggleLike(ARTICLE_ID, mockMember))
                .isInstanceOf(DomainException.class)
                .hasMessage(ClubMemberValidationErrorCode.CLUB_MEMBER_NOT_FOUND.message());
    }
}
