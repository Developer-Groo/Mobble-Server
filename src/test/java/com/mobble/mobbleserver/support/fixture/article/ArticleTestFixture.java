package com.mobble.mobbleserver.support.fixture.article;

import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.refactor.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.support.fixture.club.ClubTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;

public class ArticleTestFixture {

    public static Article createDefaultArticle() {
        ClubCategory category = ClubCategory.createClubCategory("SOCCER");
        Club club = ClubTestFixture.createDefaultClub(category);

        return Article.createArticle(
                club,
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
