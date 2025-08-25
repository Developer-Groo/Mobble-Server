package com.mobble.mobbleserver.domain.article.service;

import com.mobble.mobbleserver.domain.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.article.repository.dto.ArticleLikeInfoDto;
import com.mobble.mobbleserver.domain.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.club.validator.ClubValidator;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.comment.service.CommentService;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.like.commentLike.repository.CommentLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private CommentService commentService;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;
    @Mock
    private ArticleLikeRepository articleLikeRepository;

    @Mock
    private ArticleValidator articleValidator;
    @Mock
    private ClubValidator clubValidator;
    @Mock
    private ClubMemberValidator clubMemberValidator;
    @Mock
    private MemberValidator memberValidator;

    @InjectMocks
    private ArticleService articleService;

    @Nested
    @DisplayName("게시글 생성")
    class CreateArticle {

        @Test
        @DisplayName("게시글 생성 성공")
        void success_when_create_article() {
            // given
            Long memberId = 1L;
            Long clubId = 2L;
            ArticleRequestDto reqDto = new ArticleRequestDto("title", ArticleType.FREE, "content");

            Member mockMember = mock(Member.class);
            given(mockMember.getId()).willReturn(memberId);
            given(mockMember.getName()).willReturn("작성자");

            Club mockClub = mock(Club.class);
            given(mockClub.getId()).willReturn(clubId);

            ClubMember mockClubMember = mock(ClubMember.class);
            given(mockClubMember.canPost(ArticleType.FREE)).willReturn(true);

            given(memberValidator.findMemberByMemberIdOrThrow(memberId)).willReturn(mockMember);
            given(clubValidator.findClubByClubIdOrThrow(clubId)).willReturn(mockClub);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);

            given(articleRepository.save(any()))
                    .willAnswer(inv -> inv.getArgument(0));

            // when
            ArticleResponseDto articleResponseDto = articleService.createArticle(memberId, clubId, reqDto);

            // then
            assertThat(articleResponseDto).isNotNull();
            verify(articleRepository).save(any());
        }

        @Test
        @DisplayName("공지글 생성 실패 - MEMBER 권한")
        void fail_when_member_creates_notice() {
            // given
            Long memberId = 1L;
            Long clubId = 2L;
            ArticleRequestDto reqDto = new ArticleRequestDto("title", ArticleType.NOTICE, "content");

            Member mockMember = mock(Member.class);
            Club mockClub = mock(Club.class);
            ClubMember mockClubMember = mock(ClubMember.class);

            given(memberValidator.findMemberByMemberIdOrThrow(memberId)).willReturn(mockMember);
            given(clubValidator.findClubByClubIdOrThrow(clubId)).willReturn(mockClub);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId)).willReturn(mockClubMember);

            given(mockClubMember.canPost(ArticleType.NOTICE)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> articleService.createArticle(memberId, clubId, reqDto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.NOTICE_NO_PERMISSION.message());
        }
    }
