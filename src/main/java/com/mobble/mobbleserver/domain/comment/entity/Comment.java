package com.mobble.mobbleserver.domain.comment.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    private String content;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children;

    @Builder(access = AccessLevel.PRIVATE)
    private Comment(Comment parent, String content) {
        this.parent = parent;
        this.content = content;
    }

    public static Comment createRootComment(String content) {
        return Comment.builder()
                .content(content)
                .build();
    }

    public static Comment createReplyComment(Comment parent, String content) {
        return Comment.builder()
                .parent(parent)
                .content(content)
                .build();
    }

    public Comment changeContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean hasParent() {
        return this.parent != null;
    }

}
