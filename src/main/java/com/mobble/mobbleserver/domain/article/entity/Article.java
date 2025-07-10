package com.mobble.mobbleserver.domain.article.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "club_member_id")
    private ClubMember clubMember;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_type")
    private ArticleType articleType;

    private String title;

    private String content;

    @Builder(access = AccessLevel.PRIVATE)
    private Article(
            ClubMember clubMember,
            ArticleType articleType,
            String title,
            String content
    ) {
        this.clubMember = clubMember;
        this.articleType = articleType;
        this.title = title;
        this.content = content;
    }

    public static Article createArticle(
            ClubMember clubMember,
            ArticleType articleType,
            String title,
            String content
    ) {
        return Article.builder()
                .clubMember(clubMember)
                .articleType(articleType)
                .title(title)
                .content(content)
                .build();
    }
}
