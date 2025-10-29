package com.mobble.mobbleserver.domain.comment;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id",  nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children =  new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(
            Article article,
            Member member,
            Comment parent,
            String content
    ) {
        this.article = article;
        this.member = member;
        this.parent = parent;
        this.content = content;
    }

    public static Comment createRootComment(Member member, Article article, String content) {
        assertCommon(member, article, content);

        return Comment.builder()
                .member(member)
                .article(article)
                .parent(null)
                .content(content)
                .build();
    }

    public static Comment createReplyComment(
            Member member,
            Article article,
            Comment parent,
            String content
    ) {
        assertCommon(member, article, content);
        assertParent(parent);

        return Comment.builder()
                .member(member)
                .article(article)
                .parent(parent)
                .content(content)
                .build();
    }

    public Comment updateContent(String content) {
        assertContent(content);
        this.content = content;
        return this;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    /* Assert 검증 */
    private static void assertCommon(Member member, Article article, String content) {
        requireNonNull(member, "member must not be null");
        requireNonNull(article, "article must not be null");
        requireNonNull(content, "content must not be null");
        hasText(content, "content must not be empty");
    }

    private static void assertContent(String content) {
        requireNonNull(content, "content must not be null");
        hasText(content, "content must not be empty");
    }

    private static void assertParent(Comment comment) {
        requireNonNull(comment, "comment must not be null");
    }
}
