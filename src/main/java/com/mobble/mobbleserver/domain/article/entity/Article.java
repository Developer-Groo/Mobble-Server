package com.mobble.mobbleserver.domain.article.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.domain.club.entity.Club;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.member.entity.Member;
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
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "article_type")
    private ArticleType articleType;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Builder(access = AccessLevel.PRIVATE)
    private Article(
            Club club,
            Member member,
            ArticleType articleType,
            String title,
            String content
    ) {
        this.club = club;
        this.member = member;
        this.articleType = articleType;
        this.title = title;
        this.content = content;
    }

    public static Article createArticle(
            Club club,
            Member member,
            ArticleType articleType,
            String title,
            String content
    ) {
        return Article.builder()
                .club(club)
                .member(member)
                .articleType(articleType)
                .title(title)
                .content(content)
                .build();
    }
}