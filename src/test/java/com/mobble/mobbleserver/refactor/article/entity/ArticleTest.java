package com.mobble.mobbleserver.refactor.article.entity;

import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.refactor.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.support.fixture.article.ArticleTestFixture;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.clubCategory.ClubCategoryTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleTest {

    private final Article mockArticle = ArticleTestFixture.createDefaultArticle();
    private final Member mockMember = MemberTestFixture.createDefaultMember();
    private final ClubCategory mockClubCategory = ClubCategoryTestFixture.createDefaultCategory();
    private final Club mockClub = ClubTestFixture.createDefaultClub(mockClubCategory);
    private final ArticleType articleType = ArticleType.FREE;
    private static final String TITLE = "title";
    private static final String CONTENT = "content";


    @Nested
    @DisplayName("게시글 생성 테스트")
    class CreateArticle {

        @Test
        @DisplayName("정상 생성")
        void create_success() {
            // given & when
            Article article = Article.createArticle(mockClub, mockMember, articleType, TITLE, CONTENT);

            // then
            assertThat(article.getClub()).isEqualTo(mockClub);
            assertThat(article.getMember()).isEqualTo(mockMember);
            assertThat(article.getArticleType()).isEqualTo(ArticleType.FREE);
            assertThat(article.getTitle()).isEqualTo(TITLE);
            assertThat(article.getContent()).isEqualTo(CONTENT);
        }

        @Test
        @DisplayName("club == null → 예외")
        void create_fail_when_club_null() {
            assertThatThrownBy(() -> Article.createArticle(null, mockMember, articleType, TITLE, CONTENT))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.CLUB_REQUIRED.message());
        }

        @Test
        @DisplayName("member == null → 예외")
        void create_fail_when_member_null() {
            assertThatThrownBy(() -> Article.createArticle(mockClub, null, articleType, TITLE, CONTENT))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.MEMBER_REQUIRED.message());
        }

        @Test
        @DisplayName("articleType == null → 예외")
        void create_fail_when_type_null() {
            assertThatThrownBy(() -> Article.createArticle(mockClub, mockMember, null, TITLE, CONTENT))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.TYPE_REQUIRED.message());
        }

        @Test
        @DisplayName("title null/blank → 예외")
        void create_fail_when_title_invalid() {
            assertThatThrownBy(() -> Article.createArticle(mockClub, mockMember, articleType, null, CONTENT))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.TITLE_REQUIRED.message());

            assertThatThrownBy(() -> Article.createArticle(mockClub, mockMember, articleType, "   ", CONTENT))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.TITLE_REQUIRED.message());
        }

        @Test
        @DisplayName("content null/blank → 예외")
        void create_fail_when_content_invalid() {
            assertThatThrownBy(() -> Article.createArticle(mockClub, mockMember, articleType, TITLE, null))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.CONTENT_REQUIRED.message());

            assertThatThrownBy(() -> Article.createArticle(mockClub, mockMember, articleType, TITLE, "   "))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.CONTENT_REQUIRED.message());
        }
    }

    @Nested
    @DisplayName("아티클 수정 테스트")
    class UpdateArticle {

        @Test
        @DisplayName("아티클 수정 성공")
        void update_success() {
            // given & when
            mockArticle.updateArticle(ArticleType.REVIEW, "new_title", "new_content");

            // then
            assertThat(mockArticle.getArticleType()).isEqualTo(ArticleType.REVIEW);
            assertThat(mockArticle.getTitle()).isEqualTo("new_title");
            assertThat(mockArticle.getContent()).isEqualTo("new_content");
        }

        @Test
        @DisplayName("type == null → 예외")
        void update_fail_when_type_null() {
            // when & then
            assertThatThrownBy(() -> mockArticle.updateArticle(null, "new_title", "new_content"))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.TYPE_REQUIRED.message());
        }

        @Test
        @DisplayName("title null/blank → 예외")
        void update_fail_when_title_invalid() {
            assertThatThrownBy(() -> mockArticle.updateArticle(ArticleType.REVIEW, null, "new_content"))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.TITLE_REQUIRED.message());

            assertThatThrownBy(() -> mockArticle.updateArticle(ArticleType.REVIEW, "   ", "new_content"))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.TITLE_REQUIRED.message());
        }

        @Test
        @DisplayName("content null/blank → 예외")
        void update_fail_when_content_invalid() {
            // when & then
            assertThatThrownBy(() -> mockArticle.updateArticle(ArticleType.REVIEW, "new_title", null))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.CONTENT_REQUIRED.message());

            assertThatThrownBy(() -> mockArticle.updateArticle(ArticleType.REVIEW, "new_title", "   "))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ArticleErrorCode.CONTENT_REQUIRED.message());
        }
    }
}
