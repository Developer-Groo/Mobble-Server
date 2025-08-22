package com.mobble.mobbleserver.domain.article.entity;

import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.support.fixture.article.ArticleTestFixture;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.clubCategory.ClubCategoryTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleTest {

    private final Article mockArticle = ArticleTestFixture.createDefaultArticle();
    private final Member mockMember = MemberTestFixture.createDefaultMember();
    private final ClubCategory mockClubCategory = ClubCategoryTestFixture.createDefaultCategory();
    private final Club mockClub = ClubTestFixture.createDefaultClub(mockClubCategory);
    private final ArticleType articleType = ArticleType.FREE;
    private final String title = "title";
    private final String content = "content";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mockMember, "id", 1L);
    }

    @Nested
    @DisplayName("게시글 생성 테스트")
    class CreateArticle {

        @Test
        @DisplayName("정상 생성")
        void create_success() {
            // given & when
            Article article = Article.createArticle(mockClub, mockMember, articleType, title, content);

            // then
            assertThat(article.getClub()).isEqualTo(mockClub);
            assertThat(article.getMember()).isEqualTo(mockMember);
            assertThat(article.getArticleType()).isEqualTo(ArticleType.FREE);
            assertThat(article.getTitle()).isEqualTo(title);
            assertThat(article.getContent()).isEqualTo(content);
        }
