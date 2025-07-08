package com.mobble.mobbleserver.domain.comment.entity;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentErrorCode;
import com.mobble.mobbleserver.support.fixture.article.ArticleTestFixture;
import com.mobble.mobbleserver.support.fixture.comment.CommentTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentTest {

    private final Comment mockComment = CommentTestFixture.createDefaultRootComment();
    private final Member mockMember = MemberTestFixture.createDefaultMember();
    private final Article mockArticle = ArticleTestFixture.createDefaultArticle();
    private final String content = "content";

    @Nested
    @DisplayName("댓글 생성 테스트")
    class CreateComment {

        @Test
        @DisplayName("루트 댓글 생성 성공")
        void success_when_create_root_comment() {
            // given & when
            Comment rootComment = Comment.createRootComment(mockMember, mockArticle, content);

            // then
            assertThat(rootComment.getMember()).isEqualTo(mockMember);
            assertThat(rootComment.getArticle()).isEqualTo(mockArticle);
            assertThat(rootComment.getContent()).isEqualTo(content);
            assertThat(rootComment.hasParent()).isFalse();
        }

        @Test
        @DisplayName("대댓글 생성 성공")
        void success_when_create_reply_comment() {
            // given & when
            Comment rootComment = Comment.createRootComment(mockMember, mockArticle, content);
            Comment replyComment = Comment.createReplyComment(mockMember, mockArticle, rootComment, content);

            // then
            assertThat(replyComment.getMember()).isEqualTo(mockMember);
            assertThat(replyComment.getArticle()).isEqualTo(mockArticle);
            assertThat(replyComment.getContent()).isEqualTo(content);
            assertThat(replyComment.getParent()).isEqualTo(rootComment);
            assertThat(replyComment.hasParent()).isTrue();
        }

        @Test
        @DisplayName("대댓글의 루트 댓글이 null 인 경우 예외 발생")
        void fails_when_create_reply_comment_parent_is_null() {
            // when & then
            assertThatThrownBy(() -> Comment.createReplyComment(mockMember, mockArticle, null, content))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(CommentErrorCode.PARENT_REQUIRED.message());
        }
    }

    @Nested
    @DisplayName("댓글 내용 변경 테스트")
    class changeContent {

        @Test
        @DisplayName("댓글 내용 변경 성공")
        void success_when_change_content() {
            // given & when
            mockComment.changeContent("newContent");

            // then
            assertThat(mockComment.getContent()).isEqualTo("newContent");
        }

        @Test
        @DisplayName("null 내용으로 변경 시 예외 발생")
        void fails_when_change_content_is_null() {
            // when & then
            assertThatThrownBy(() -> mockComment.changeContent(null))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(CommentErrorCode.CONTENT_REQUIRED.message());
        }

        @Test
        @DisplayName("공백 내용으로 변경 시 예외 발생")
        void fails_when_change_content_is_blank() {
            // when & then
            assertThatThrownBy(() -> mockComment.changeContent("  "))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(CommentErrorCode.CONTENT_REQUIRED.message());
        }
    }

    @Nested
    @DisplayName("댓글 생성 유효성 검증 테스트")
    class Validation {

        @Test
        @DisplayName("member 가 null 인 경우 예외 발생")
        void fails_with_null_member() {
            // when & then
            assertThatThrownBy(() -> Comment.createRootComment(null, mockArticle, content))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(CommentErrorCode.MEMBER_REQUIRED.message());
        }

        @Test
        @DisplayName("article 이 null 인 경우 예외 발생")
        void fails_with_null_article() {
            // when & then
            assertThatThrownBy(() -> Comment.createRootComment(mockMember, null, content))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(CommentErrorCode.ARTICLE_REQUIRED.message());
        }

        @Test
        @DisplayName("content 가 null 인 경우 예외 발생")
        void fails_with_null_content() {
            // when & then
            assertThatThrownBy(() -> Comment.createRootComment(mockMember, mockArticle, null))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(CommentErrorCode.CONTENT_REQUIRED.message());
        }
    }
}