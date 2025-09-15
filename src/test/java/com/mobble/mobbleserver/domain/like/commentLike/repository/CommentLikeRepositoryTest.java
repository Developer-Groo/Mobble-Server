package com.mobble.mobbleserver.domain.like.commentLike.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.like.commentLike.entity.CommentLike;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.article.ArticleTestFixture;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
class CommentLikeRepositoryTest {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("댓글에 사용자가 좋아요를 누른 경우, Comment Like 조회 성공")
    void success_when_liked() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(clubCategory);
        Article article = ArticleTestFixture.createWithMemberAndClub(member, club);
        Comment comment = Comment.createRootComment(member, article, "hello");

        em.persist(clubCategory);
        em.persist(member);
        em.persist(club);
        em.persist(article);
        em.persist(comment);

        CommentLike like = CommentLike.createCommentLike(comment, member);
        em.persist(like);
        em.flush();
        em.clear();

        // when
        Optional<CommentLike> result = commentLikeRepository.findLikedByCommentIdAndMemberId(comment.getId(), member.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getComment().getId()).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("댓글에 좋아요를 누르지 않은 경우, CommentLike 조회 결과 없음")
    void success_when_not_liked() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(clubCategory);
        Article article = ArticleTestFixture.createWithMemberAndClub(member, club);
        Comment comment = Comment.createRootComment(member, article, "hello");

        em.persist(clubCategory);
        em.persist(member);
        em.persist(club);
        em.persist(article);
        em.persist(comment);
        em.flush();
        em.clear();

        // when
        Optional<CommentLike> result = commentLikeRepository.findLikedByCommentIdAndMemberId(comment.getId(), member.getId());

        // then
        assertThat(result).isNotPresent();
    }
}
