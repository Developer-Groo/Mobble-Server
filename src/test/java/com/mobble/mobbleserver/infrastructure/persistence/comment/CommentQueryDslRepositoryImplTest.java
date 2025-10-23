package com.mobble.mobbleserver.infrastructure.persistence.comment;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.infrastructure.persistence.comment.projection.CommentLikeInfoDto;
import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.refactor.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.refactor.like.commentLike.entity.CommentLike;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.support.fixture.article.ArticleTestFixture;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class CommentQueryDslRepositoryImplTest {

    @Autowired
    private JpaCommentRepository commentRepository;

    @Autowired
    private EntityManager em;

    @Test
    void success_when_find_comments_with_replies_by_article_id() {
        Member member = MemberTestFixture.createDefaultMember();
        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        em.persist(category);

        Club club = ClubTestFixture.createDefaultClub(category);
        Article article = ArticleTestFixture.createWithMemberAndClub(member, club);
        em.persist(member);
        em.persist(club);
        em.persist(article);

        Comment firstRootComment = Comment.createRootComment(member, article, "parent comment-1");
        Comment secondeRootComment = Comment.createRootComment(member, article, "parent comment-2");
        Comment firstReplyComment = Comment.createReplyComment(member, article, firstRootComment, "reply comment-1");
        Comment secondReplyComment = Comment.createReplyComment(member, article, secondeRootComment, "reply comment-2");
        em.persist(firstRootComment);
        em.persist(secondeRootComment);
        em.persist(firstReplyComment);
        em.persist(secondReplyComment);
        em.flush();
        em.clear();

        List<Comment> commentList = commentRepository.findCommentsWithRepliesByArticleId(article.getId());

        assertThat(commentList).hasSize(2);
        assertThat(commentList).extracting(Comment::getContent)
                .containsExactlyInAnyOrder(firstRootComment.getContent(), secondeRootComment.getContent());

        assertThat(commentList.get(0).getChildren()).hasSize(1);
        assertThat(commentList.get(1).getChildren()).hasSize(1);

        assertThat(commentList.get(0).getChildren()).extracting(Comment::getContent)
                .containsExactlyInAnyOrder(firstReplyComment.getContent());

        assertThat(commentList.get(1).getChildren()).extracting(Comment::getContent)
                .containsExactlyInAnyOrder(secondReplyComment.getContent());
    }

    @Test
    void success_when_find_like_info_by_comment_ids_and_member_id() {
        Member member = MemberTestFixture.createDefaultMember();
        Member otherMember = MemberTestFixture.createDefaultMember();
        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        em.persist(category);

        Club club = ClubTestFixture.createDefaultClub(category);
        Article article = ArticleTestFixture.createWithMemberAndClub(member, club);
        em.persist(member);
        em.persist(otherMember);
        em.persist(club);
        em.persist(article);

        Comment comment1 = Comment.createRootComment(member, article, "comment-1");
        Comment comment2 = Comment.createRootComment(member, article, "comment-2");
        em.persist(comment1);
        em.persist(comment2);

        CommentLike like1 = CommentLike.createCommentLike(comment1, member);
        CommentLike like2 = CommentLike.createCommentLike(comment1, otherMember);
        CommentLike like3 = CommentLike.createCommentLike(comment2, otherMember);
        em.persist(like1);
        em.persist(like2);
        em.persist(like3);

        em.flush();
        em.clear();

        List<Long> commentsId = List.of(comment1.getId(), comment2.getId());
        Map<Long, CommentLikeInfoDto> likeInfoMap = commentRepository.findLikeInfoByCommentIdsAndMemberId(commentsId, member.getId());

        assertThat(likeInfoMap).hasSize(2);

        CommentLikeInfoDto info1 = likeInfoMap.get(comment1.getId());
        assertThat(info1.likeCount()).isEqualTo(2);
        assertThat(info1.isLiked()).isTrue();

        CommentLikeInfoDto info2 = likeInfoMap.get(comment2.getId());
        assertThat(info2.likeCount()).isEqualTo(1);
        assertThat(info2.isLiked()).isFalse();
    }
}
