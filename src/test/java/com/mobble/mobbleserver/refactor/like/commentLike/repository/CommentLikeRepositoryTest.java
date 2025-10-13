package com.mobble.mobbleserver.refactor.like.commentLike.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.club.core.entity.Club;
import com.mobble.mobbleserver.refactor.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.refactor.like.commentLike.entity.CommentLike;
import com.mobble.mobbleserver.domain.member.Member;
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
class CommentLikeRepositoryTest {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private EntityManager em;

    @Nested
    @DisplayName("좋아요 조회 기능")
    class Find {

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

    @Nested
    @DisplayName("좋아요 삭제 기능")
    class Delete {

        @Test
        @DisplayName("Article ID로 댓글 좋아요 전체 삭제 성공")
        void success_when_delete_all_by_article_id() {
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);
            Article article = ArticleTestFixture.createWithMemberAndClub(member, club);
            Comment comment1 = Comment.createRootComment(member, article, "content1");
            Comment comment2 = Comment.createRootComment(member, article, "content2");

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);
            em.persist(article);
            em.persist(comment1);
            em.persist(comment2);

            CommentLike commentLike1 = CommentLike.createCommentLike(comment1, member);
            CommentLike commentLike2 = CommentLike.createCommentLike(comment2, member);
            em.persist(commentLike1);
            em.persist(commentLike2);
            em.flush();

            // when
            commentLikeRepository.deleteAllByArticleId(article.getId());
            em.flush();
            em.clear();

            // then
            assertThat(commentLikeRepository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("Article ID로 삭제 시, 해당 Article 의 댓글 좋아요가 없는 경우에도 예외 없이 삭제 성공")
        void success_when_delete_all_by_article_id_but_nothing_to_delete() {
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

            // when
            commentLikeRepository.deleteAllByArticleId(article.getId());
            em.flush();

            // then
            assertThat(commentLikeRepository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("Article ID 리스트로 댓글 좋아요 전체 삭제 성공")
        void success_when_delete_all_by_article_ids() {
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);
            Article article1 = ArticleTestFixture.createWithMemberAndClub(member, club);
            Article article2 = ArticleTestFixture.createWithMemberAndClub(member, club);
            Comment comment1 = Comment.createRootComment(member, article1, "content1");
            Comment comment2 = Comment.createRootComment(member, article2, "content2");

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);
            em.persist(article1);
            em.persist(article2);
            em.persist(comment1);
            em.persist(comment2);

            CommentLike commentLike1 = CommentLike.createCommentLike(comment1, member);
            CommentLike commentLike2 = CommentLike.createCommentLike(comment1, member);
            em.persist(commentLike1);
            em.persist(commentLike2);
            em.flush();

            List<Long> articleIds = List.of(article1.getId(), article2.getId());

            // when
            commentLikeRepository.deleteAllCommentLikeByComment_Article_IdIn(articleIds);
            em.flush();
            em.clear();

            // then
            assertThat(commentLikeRepository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("Article ID 리스트로 삭제 시, 해당 Article 의 댓글 좋아요가 없는 경우에도 예외 없이 삭제 성공")
        void success_when_delete_all_by_article_ids_but_nothing_to_delete() {
            // given
            Member member = MemberTestFixture.createDefaultMember();
            ClubCategory clubCategory = ClubCategory.createClubCategory("SOCCER");
            Club club = ClubTestFixture.createDefaultClub(clubCategory);
            Article article1 = ArticleTestFixture.createWithMemberAndClub(member, club);
            Article article2 = ArticleTestFixture.createWithMemberAndClub(member, club);

            em.persist(clubCategory);
            em.persist(member);
            em.persist(club);
            em.persist(article1);
            em.persist(article2);
            em.flush();

            List<Long> articleIds = List.of(article1.getId(), article2.getId());

            // when
            commentLikeRepository.deleteAllCommentLikeByComment_Article_IdIn(articleIds);
            em.flush();

            // then
            assertThat(commentLikeRepository.findAll()).isEmpty();
        }
    }
}
