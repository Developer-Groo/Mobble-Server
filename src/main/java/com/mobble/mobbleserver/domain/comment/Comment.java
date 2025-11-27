package com.mobble.mobbleserver.domain.comment;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.comment.error.CommentError;
import com.mobble.mobbleserver.domain.common.BaseEntity;
import com.mobble.mobbleserver.domain.exception.DomainException;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

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
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Embedded
    private CommentContent content;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(
            Article article,
            Member member,
            Comment parent,
            CommentContent content
    ) {
        this.article = article;
        this.member = member;
        this.parent = parent;
        this.content = content;
    }

    public static Comment createRootComment(Member member, Article article, CommentContent body) {
        assertRoot(member, article);

        return Comment.builder()
                .member(member)
                .article(article)
                .parent(null)
                .content(body)
                .build();
    }

    public static Comment createReplyComment(
            Member member,
            Comment parent,
            CommentContent body
    ) {
        assertReply(member, parent);

        Comment child = Comment.builder()
                .member(member)
                .article(parent.article)
                .parent(parent)
                .content(body)
                .build();

        parent.addChild(child);

        return child;
    }

    public Comment updateContent(CommentContent content) {
        requireNonNull(content, "content must not be null");

        this.content = content;

        return this;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public boolean isOwner(Long memberId) {
        requireNonNull(member, "member must not be null");

        return this.member.getId().equals(memberId);
    }

    private void addChild(Comment child) {
        requireNonNull(child, "child must not be null");

        child.parent = this;
        this.children.add(child);
    }

    /* Assert 검증 */
    private static void assertRoot(Member member, Article article) {
        requireNonNull(member, "member must not be null");
        requireNonNull(article, "article must not be null");
    }

    private static void assertReply(Member member, Comment parent) {
        requireNonNull(member, "member must not be null");
        requireNonNull(parent, "parent must not be null");
        requireNonNull(parent.article, "parent.article must not be null");

        if (parent.hasParent()) throw new DomainException(CommentError.INVALID_PARENT);
    }
}
