package com.mobble.mobbleserver.domain.like.articleLike.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

        given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, MEMBER_ID))
                .willReturn(mockClubMember);
        given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID))
                .willReturn(mockArticle);
        given(articleLikeRepository.findLikedByArticleIdAndMemberId(ARTICLE_ID, MEMBER_ID))
                .willReturn(Optional.empty());

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

        given(articleValidator.findArticleByArticleIdOrThrow(ARTICLE_ID))
                .willReturn(mockArticle);
        given(articleLikeRepository.findLikedByArticleIdAndMemberId(ARTICLE_ID, MEMBER_ID))
                .willReturn(Optional.of(mockArticleLike));

        // when
        boolean result = articleLikeService.toggleLike(ARTICLE_ID, mockMember).isLiked();

        // then
        verify(articleLikeRepository).delete(mockArticleLike);
        assertThat(result).isFalse();
    }
}
