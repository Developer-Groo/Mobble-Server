package com.mobble.mobbleserver.support.fixture.article;

import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleContent;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;

public class ArticleTestFixture {

    public static Article createDefaultArticle() {
        return Article.createArticle(
                null,
                MemberTestFixture.createDefaultMember(),
                ArticleType.FREE,
                ArticleContent.of("title", "body")
        );
    }

    public static Article createWithMemberAndClub(Member member, Club club) {
        return Article.createArticle(
                club,
                member,
                ArticleType.FREE,
                ArticleContent.of("title", "body")
        );
    }
}
