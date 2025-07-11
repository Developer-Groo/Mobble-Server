package com.mobble.mobbleserver.domain.comment.repository;

import com.mobble.mobbleserver.config.QueryDslConfig;
import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.club.entity.Club;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class CommentRepositoryImplTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("게시글 ID 로 댓글과 대댓글 조회 성공")
    void success_when_find_comments_with_replies_by_article_id() {
        // given
        Member member = MemberTestFixture.createDefaultMember();
        Club club = ClubTestFixture.createDefaultClub();
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

        // when
        List<Comment> commentList = commentRepository.findCommentsWithRepliesByArticleId(article.getId());

        // then
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

}