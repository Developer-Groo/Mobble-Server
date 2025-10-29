package com.mobble.mobbleserver.refactor.like.articleLike.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.ClubCategory.ClubCategory;
import com.mobble.mobbleserver.domain.like.core.ArticleLike;
import com.mobble.mobbleserver.infrastructure.persistence.like.core.articleLike.JpaArticleLikeRepository;
import com.mobble.mobbleserver.support.fixture.article.ArticleTestFixture;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class JpaArticleLikeRepositoryTest {

    @Autowired
    private JpaArticleLikeRepository jpaArticleLikeRepository;

    @Autowired
    private EntityManager em;

    @Nested
    @DisplayName("좋아요 조회 기능")
    class Find {

        @Test
        @DisplayName("게시글에 사용자가 좋아요를 누른 경우, ArticleLike 조회 성공")
        void success_when_liked() {
            // given
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);
            Article article = ArticleTestFixture.createWithMemberAndClub(member, club);
            ArticleLike articleLike = ArticleLike.createArticleLike(member.getId(), article.getId());

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);
            em.persist(article);
            em.persist(articleLike);
            em.flush();
            em.clear();

            // when
            Optional<ArticleLike> result = jpaArticleLikeRepository.findLikedByArticleIdAndMemberId(article.getId(), member.getId());

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getMemberId()).isEqualTo(member.getId());
            assertThat(result.get().getArticleId()).isEqualTo(article.getId());
        }

        @Test
        @DisplayName("게시글에 사용자가 좋아요를 누르지 않은 경우, ArticleLike 조회 결과 없음")
        void success_when_not_liked() {
            // given
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);
            Article article = ArticleTestFixture.createWithMemberAndClub(member, club);

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);
            em.persist(article);
            em.flush();
            em.clear();

            // when
            Optional<ArticleLike> result = jpaArticleLikeRepository.findLikedByArticleIdAndMemberId(article.getId(), member.getId());

            // then
            assertThat(result).isNotPresent();
        }

        @Test
        @DisplayName("Article ID로 ArticleLike 리스트 조회 성공")
        void success_when_liked_list_by_article_id() {
            // given
            Member member1 = MemberTestFixture.createDefaultMember();
            Member member2 = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);
            Article article = ArticleTestFixture.createWithMemberAndClub(member1, club);

            em.persist(clubCategory);
            em.persist(member1);
            em.persist(member2);
            em.persist(club);
            em.persist(article);

            ArticleLike articleLike1 = ArticleLike.createArticleLike(member1.getId(), article.getId());
            ArticleLike articleLike2 = ArticleLike.createArticleLike(member2.getId(), article.getId());
            em.persist(articleLike1);
            em.persist(articleLike2);
            em.flush();
            em.clear();

            // when
            List<ArticleLike> articleLikes = jpaArticleLikeRepository.findAllByArticleId(article.getId());

            // then
            assertThat(articleLikes).hasSize(2);
            assertThat(articleLikes).extracting(ArticleLike::getMemberId)
//                    .extracting(Member::getId)
                    .containsExactlyInAnyOrder(member1.getId(), member2.getId());
        }

        @Test
        @DisplayName("Article ID로 ArticleLike 리스트 조회 시 좋아요가 없는 경우, 빈 리스트 반환")
        void success_when_returns_empty_list_by_article_id() {
            // given
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("축구");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);
            Article article = ArticleTestFixture.createWithMemberAndClub(member, club);

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);
            em.persist(article);
            em.flush();
            em.clear();

            // when
            List<ArticleLike> articleLikes = jpaArticleLikeRepository.findAllByArticleId(article.getId());

            // then
            assertThat(articleLikes)
                    .isNotNull()
                    .isEmpty();
        }
    }

    @Nested
    @DisplayName("좋아요 삭제 기능")
    class Delete {

        @Test
        @DisplayName("Article ID로 모든 좋아요 삭제 성공")
        void success_when_delete_all_by_article_id() {
            // given
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);
            Article article = ArticleTestFixture.createWithMemberAndClub(member, club);

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);
            em.persist(article);

            ArticleLike articleLike1 = ArticleLike.createArticleLike(member.getId(), article.getId());
            ArticleLike articleLike2 = ArticleLike.createArticleLike(member.getId(), article.getId());
            em.persist(articleLike1);
            em.persist(articleLike2);
            em.flush();

            // when
            jpaArticleLikeRepository.deleteAllByArticleId(article.getId());
            em.flush();
            em.clear();

            // then
            List<ArticleLike> result = jpaArticleLikeRepository.findAllByArticleId(article.getId());
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Article ID 리스트로 좋아요 전체 삭제 성공")
        void success_when_delete_all_by_article_ids() {
            // given
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);

            Article article1 = ArticleTestFixture.createWithMemberAndClub(member, club);
            Article article2 = ArticleTestFixture.createWithMemberAndClub(member, club);
            em.persist(article1);
            em.persist(article2);

            ArticleLike articleLike1 = ArticleLike.createArticleLike(member.getId(), article1.getId());
            ArticleLike articleLike2 = ArticleLike.createArticleLike(member.getId(), article2.getId());
            em.persist(articleLike1);
            em.persist(articleLike2);
            em.flush();

            List<Long> articleIds = List.of(article1.getId(), article2.getId());

            // when
            jpaArticleLikeRepository.deleteAllByArticleIdIn(articleIds);
            em.flush();
            em.clear();

            // then
            List<ArticleLike> remainingLikes = jpaArticleLikeRepository.findAll();
            assertThat(remainingLikes).isEmpty();
        }
    }
}
