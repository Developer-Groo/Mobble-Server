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
}
