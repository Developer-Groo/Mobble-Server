package com.mobble.mobbleserver.domain.article.entity;

import com.mobble.mobbleserver.common.baseEntity.BaseEntity;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
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
    @Column(name = "article_type")
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
        validateCommon(club, member, articleType, title, content);
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

    public void updateArticle(ArticleType articleType, String title, String content) {
        validateContent(articleType, title, content);
        this.articleType = articleType;
        this.title = title;
        this.content = content;
    }

    private void validateCommon(
            Club club,
            Member member,
            ArticleType articleType,
            String title,
            String content
    ) {
        if (club == null) throw new DomainException(ArticleErrorCode.CLUB_REQUIRED);
        if (member == null) throw new DomainException(ArticleErrorCode.MEMBER_REQUIRED);
        validateContent(articleType, title, content);
    }

    private void validateContent(ArticleType articleType, String title, String content) {
        if (articleType == null) throw new DomainException(ArticleErrorCode.TYPE_REQUIRED);
        if (title == null || title.isBlank()) throw new DomainException(ArticleErrorCode.TITLE_REQUIRED);
        if (content == null || content.isBlank()) throw new DomainException(ArticleErrorCode.CONTENT_REQUIRED);
    }

    public boolean isWrittenBy(Long memberId) {
        return this.getMember().getId().equals(memberId);
    }
}
