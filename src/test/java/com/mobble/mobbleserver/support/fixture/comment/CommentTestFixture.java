package com.mobble.mobbleserver.support.fixture.comment;

import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.support.fixture.article.ArticleTestFixture;
import com.mobble.mobbleserver.support.fixture.member.MemberTestFixture;

public class CommentTestFixture {

    public static Comment createDefaultRootComment() {
        return  Comment.createRootComment(MemberTestFixture.createDefaultMember(), ArticleTestFixture.createDefaultArticle(), "content");
    }
}
