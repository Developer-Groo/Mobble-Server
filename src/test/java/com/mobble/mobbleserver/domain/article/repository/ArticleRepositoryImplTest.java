package com.mobble.mobbleserver.domain.article.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.article.repository.dto.ArticleLikeInfoDto;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.article.ArticleTestFixture;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class ArticleRepositoryImplTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("클럽 ID(+옵션: 타입)로 게시글 목록 조회 성공")
    void success_when_find_articles_by_club_id_with_optional_type() {
        // given
        Member writer = MemberTestFixture.createDefaultMember();
        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(category);

        em.persist(category);
        em.persist(writer);
        em.persist(club);

        Article a1 = ArticleTestFixture.createWithMemberAndClub(writer, club);
        a1.updateArticle(ArticleType.FREE, "t1", "c1");
        Article a2 = ArticleTestFixture.createWithMemberAndClub(writer, club);
        a2.updateArticle(ArticleType.NOTICE, "t2", "c2");

        em.persist(a1);
        em.persist(a2);
        em.flush();
        em.clear();

        // when
        List<Article> allByClub = articleRepository.findArticlesByClubId(club.getId(), null); // 타입 필터 없음
        List<Article> onlyFree = articleRepository.findArticlesByClubId(club.getId(), ArticleType.FREE);
        List<Article> onlyNotice = articleRepository.findArticlesByClubId(club.getId(), ArticleType.NOTICE);

        // then
        assertThat(allByClub).hasSize(2);
        assertThat(allByClub).extracting(Article::getArticleType)
                .containsExactlyInAnyOrder(ArticleType.FREE, ArticleType.NOTICE);

        assertThat(onlyFree).hasSize(1);
        assertThat(onlyFree.get(0).getArticleType()).isEqualTo(ArticleType.FREE);

        assertThat(onlyNotice).hasSize(1);
        assertThat(onlyNotice.get(0).getArticleType()).isEqualTo(ArticleType.NOTICE);
    }
