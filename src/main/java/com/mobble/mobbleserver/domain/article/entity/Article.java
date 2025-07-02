package com.mobble.mobbleserver.domain.article.entity;

import com.mobble.mobbleserver.Common.BaseEntity.BaseEntity;
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @Column(name = "club_member_id")
//    private ClubMemeber clubMemeber;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_type")
    private ArticleType articleType;

    private String title;

    private String content;

    @Builder
    private Article(
            // Todo: ClubMember 주입 필요
            ArticleType articleType,
            String title,
            String content
    ) {
        this.articleType = articleType;
        this.title = title;
        this.content = content;
    }

    public static Article createArticle(
            // Todo: ClubMember 주입 필요
            ArticleType articleType,
            String title,
            String content
    ) {
        return Article.builder()
                .articleType(articleType)
                .title(title)
                .content(content)
                .build();
    }
}
