package com.mobble.mobbleserver.domain.comment.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.errorCode.comment.CommentErrorCode;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    // Todo: 부모 댓글 삭제 했을 때, 자식 댓글 처리
    @OneToMany(mappedBy = "parent")
    private List<Comment> children;

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(
            Article article,
            Member member,
            Comment parent,
            String content
    ) {
        validateCommon(member, article, content);
        this.article = article;
        this.member = member;
        this.parent = parent;
        this.content = content;
    }

    public static Comment createRootComment(Member member, Article article, String content) {
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
        if (parent == null) throw new DomainException(CommentErrorCode.PARENT_REQUIRED);

        return Comment.builder()
                .member(member)
                .article(article)
                .parent(parent)
                .content(content)
                .build();
    }

    public Comment updateContent(String content) {
        validateContent(content);
        this.content = content;
        return this;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    private void validateCommon(Member member, Article article, String content) {
        if (member == null) throw new DomainException(CommentErrorCode.MEMBER_REQUIRED);
        if (article == null) throw new DomainException(CommentErrorCode.ARTICLE_REQUIRED);
        validateContent(content);
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) throw new DomainException(CommentErrorCode.CONTENT_REQUIRED);
    }
}
