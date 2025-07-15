package com.mobble.mobbleserver.support.fixture.article;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.club.entity.Club;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;

public class ArticleTestFixture {

    public static Article createDefaultArticle() {
        return Article.createArticle(
                ClubTestFixture.createDefaultClub(),
                MemberTestFixture.createDefaultMember(),
                ArticleType.FREE, "title",
                "content"
        );
    }

    public static Article createWithMemberAndClub(Member member, Club club) {
        return Article.createArticle(
                club,
                member,
                ArticleType.FREE, "title",
                "content"
        );
    }
}
