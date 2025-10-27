package com.mobble.mobbleserver.infrastructure.persistence.article;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.infrastructure.persistence.article.projection.ArticleLikeInfoDto;
import com.mobble.mobbleserver.domain.ClubCategory.ClubCategory;
import com.mobble.mobbleserver.refactor.like.articleLike.entity.ArticleLike;
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
    private JpaArticleRepository articleRepository;

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

    @Test
    @DisplayName("게시글 ID들로 좋아요 수 & 사용자의 좋아요 여부 조회 성공")
    void success_when_find_like_info_by_article_ids_and_member_id() {
        // given
        Member me = MemberTestFixture.createDefaultMember();
        Member other = MemberTestFixture.createDefaultMember();

        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(category);

        em.persist(category);
        em.persist(me);
        em.persist(other);
        em.persist(club);

        Article a1 = ArticleTestFixture.createWithMemberAndClub(me, club);
        a1.updateArticle(ArticleType.FREE, "a1", "c1");
        Article a2 = ArticleTestFixture.createWithMemberAndClub(me, club);
        a2.updateArticle(ArticleType.FREE, "a2", "c2");

        em.persist(a1);
        em.persist(a2);

        ArticleLike l1 = ArticleLike.createArticleLike(a1, me);
        ArticleLike l2 = ArticleLike.createArticleLike(a1, other);
        ArticleLike l3 = ArticleLike.createArticleLike(a2, other);

        em.persist(l1);
        em.persist(l2);
        em.persist(l3);

        em.flush();
        em.clear();

        // when
        List<Long> ids = List.of(a1.getId(), a2.getId());
        Map<Long, ArticleLikeInfoDto> infoForMe =
                articleRepository.findLikeInfoByArticleIdsAndMemberId(ids, me.getId());
        Map<Long, ArticleLikeInfoDto> infoForNullUser =
                articleRepository.findLikeInfoByArticleIdsAndMemberId(ids, null);

        // then
        assertThat(infoForMe).hasSize(2);
        assertThat(infoForNullUser).hasSize(2);

        ArticleLikeInfoDto a1InfoForMe = infoForMe.get(a1.getId());
        assertThat(a1InfoForMe.likeCount()).isEqualTo(2);
        assertThat(a1InfoForMe.isLiked()).isTrue();

        ArticleLikeInfoDto a2InfoForMe = infoForMe.get(a2.getId());
        assertThat(a2InfoForMe.likeCount()).isEqualTo(1);
        assertThat(a2InfoForMe.isLiked()).isFalse();

        ArticleLikeInfoDto a1InfoNull = infoForNullUser.get(a1.getId());
        assertThat(a1InfoNull.likeCount()).isEqualTo(2);
        assertThat(a1InfoNull.isLiked()).isFalse();

        ArticleLikeInfoDto a2InfoNull = infoForNullUser.get(a2.getId());
        assertThat(a2InfoNull.likeCount()).isEqualTo(1);
        assertThat(a2InfoNull.isLiked()).isFalse();
    }

    @Test
    @DisplayName("클럽 ID로 게시글 ID 목록 조회 성공")
    void success_when_find_article_ids_by_club_id() {
        // given
        Member writer = MemberTestFixture.createDefaultMember();
        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(category);

        em.persist(category);
        em.persist(writer);
        em.persist(club);

        Article a1 = ArticleTestFixture.createWithMemberAndClub(writer, club);
        a1.updateArticle(ArticleType.FREE, "a1", "c1");
        Article a2 = ArticleTestFixture.createWithMemberAndClub(writer, club);
        a2.updateArticle(ArticleType.NOTICE, "a2", "c2");

        em.persist(a1);
        em.persist(a2);
        em.flush();
        em.clear();

        // when
        List<Long> ids = articleRepository.findArticleIdsByClubId(club.getId());

        // then
        assertThat(ids).containsExactlyInAnyOrder(a1.getId(), a2.getId());
    }
}
