package com.mobble.mobbleserver.domain.article;

import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.domain.common.entity.BaseEntity;
import com.mobble.mobbleserver.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_type")
    private ArticleType articleType;

    @Embedded
    private ArticleContent content;

    @Builder(access = AccessLevel.PRIVATE)
    private Article(
            Club club,
            Member member,
            ArticleType articleType,
            ArticleContent content
    ) {
        this.club = club;
        this.member = member;
        this.articleType = articleType;
        this.content = content;
    }

    public static Article createArticle(
            Club club,
            Member member,
            ArticleType articleType,
            ArticleContent content
    ) {
        assertCreateArticle(club, member, articleType, content);

        return Article.builder()
                .club(club)
                .member(member)
                .articleType(articleType)
                .content(content)
                .build();
    }

    public void updateArticle(ArticleType articleType, ArticleContent content) {
        assertUpdateArticle(articleType, content);

        this.articleType = articleType;
        this.content = content;
    }

    private static void assertCreateArticle(
            Club club,
            Member member,
            ArticleType articleType,
            ArticleContent content
    ) {
        requireNonNull(club, "content must not be null");
        requireNonNull(member, "member must not be null");
        requireNonNull(articleType, "article type must not be null");
        requireNonNull(content, "content must not be null");
    }

    private void assertUpdateArticle(ArticleType articleType, ArticleContent content) {
        requireNonNull(articleType, "article type must not be null");
        requireNonNull(content, "content must not be null");
    }
}
