package com.mobble.mobbleserver.domain.like.articleLike.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.article.entity.Article;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class ArticleLikeRepositoryTest {

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("게시글에 사용자가 좋아요를 누른 경우, ArticleLike 조회 성공")
    void findLikedByArticleIdAndMemberId_success_when_liked() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(category);
        Article article = ArticleTestFixture.createWithMemberAndClub(member, club);
        ArticleLike like = ArticleLike.createArticleLike(article, member);

        em.persist(category);
        em.persist(member);
        em.persist(club);
        em.persist(article);
        em.persist(like);
        em.flush();
        em.clear();

        // when
        Optional<ArticleLike> result = articleLikeRepository.findLikedByArticleIdAndMemberId(article.getId(), member.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getMember().getId()).isEqualTo(member.getId());
        assertThat(result.get().getArticle().getId()).isEqualTo(article.getId());
    }

    @Test
    @DisplayName("게시글에 사용자가 좋아요를 누르지 않은 경우, ArticleLike 조회 결과 없음")
    void findLikedByArticleIdAndMemberId_success_when_not_liked() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(category);
        Article article = ArticleTestFixture.createWithMemberAndClub(member, club);

        em.persist(category);
        em.persist(member);
        em.persist(club);
        em.persist(article);
        em.flush();
        em.clear();

        // when
        Optional<ArticleLike> result = articleLikeRepository.findLikedByArticleIdAndMemberId(article.getId(), member.getId());

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("Article ID로 ArticleLike 리스트 조회 성공")
    void success_when_findAllByArticleId() {
        // given
        Member member1 = MemberTestFixture.createDefaultMember();
        Member member2 = MemberTestFixture.createDefaultMember();
        ClubCategory category = ClubCategory.createClubCategory("축구");
        Club club = ClubTestFixture.createDefaultClub(category);
        Article article = ArticleTestFixture.createWithMemberAndClub(member1, club);

        em.persist(category);
        em.persist(member1);
        em.persist(member2);
        em.persist(club);
        em.persist(article);

        ArticleLike like1 = ArticleLike.createArticleLike(article, member1);
        ArticleLike like2 = ArticleLike.createArticleLike(article, member2);
        em.persist(like1);
        em.persist(like2);
        em.flush();
        em.clear();

        // when
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByArticleId(article.getId());

        // then
        assertThat(articleLikes).hasSize(2);
        assertThat(articleLikes).extracting(ArticleLike::getMember)
                .extracting(Member::getId)
                .containsExactlyInAnyOrder(member1.getId(), member2.getId());
    }

    @Test
    @DisplayName("Article ID로 ArticleLike 리스트 조회 시 좋아요가 없는 경우, 빈 리스트 반환")
    void success_when_findAllByArticleId_returns_empty_list() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        ClubCategory category = ClubCategory.createClubCategory("축구");
        Club club = ClubTestFixture.createDefaultClub(category);
        Article article = ArticleTestFixture.createWithMemberAndClub(member, club);

        em.persist(category);
        em.persist(member);
        em.persist(club);
        em.persist(article);
        em.flush();
        em.clear();

        // when
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByArticleId(article.getId());

        // then
        assertThat(articleLikes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Article ID로 모든 좋아요 삭제 성공")
    void success_when_delete_all_by_article_id() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(category);
        Article article = ArticleTestFixture.createWithMemberAndClub(member, club);

        em.persist(category);
        em.persist(member);
        em.persist(club);
        em.persist(article);

        ArticleLike like1 = ArticleLike.createArticleLike(article, member);
        ArticleLike like2 = ArticleLike.createArticleLike(article, member);
        em.persist(like1);
        em.persist(like2);
        em.flush();

        // when
        articleLikeRepository.deleteAllByArticleId(article.getId());
        em.flush();
        em.clear();

        // then
        List<ArticleLike> result = articleLikeRepository.findAllByArticleId(article.getId());
        assertThat(result).isEmpty();
    }
}
