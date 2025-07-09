package com.mobble.mobbleserver.domain.comment.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.comment.validator.CommentValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentValidator commentValidator;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentService commentService;

    @Nested
    @DisplayName("댓글 생성")
    class CreateComment {

        @Test
        @DisplayName("루트 댓글 생성 성공")
        void success_when_create_root_comment() {
            // given
            Long memberId = 1L;
            Long articleId = 2L;
            CommentRequestDto dto = new CommentRequestDto("content");
            Member mockMember = mock(Member.class);
            Article mockArticle = mock(Article.class);
            Comment mockComment = mock(Comment.class);

            given(memberRepository.findById(memberId)).willReturn(Optional.of(mockMember));
            given(articleRepository.findById(articleId)).willReturn(Optional.of(mockArticle));
            given(commentRepository.save(any())).willReturn(mockComment);
            given(mockComment.getMember()).willReturn(mockMember);
            given(mockComment.getArticle()).willReturn(mockArticle);

            // when
            CommentResponseDto rootComment = commentService.createRootComment(memberId, articleId, dto);

            // then
            assertThat(rootComment).isNotNull();
            verify(commentRepository).save(any());
        }
    }
}