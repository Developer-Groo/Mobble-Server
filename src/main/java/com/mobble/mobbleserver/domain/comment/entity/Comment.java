package com.mobble.mobbleserver.domain.comment.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

//    Todo: 구현 완료되면 주석 해제
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "article_id")
//    private Article article;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "name_id")
//    private Member member;

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

}
