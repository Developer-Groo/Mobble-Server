package com.mobble.mobbleserver.domain.like.baseLike.entity;

import com.mobble.mobbleserver.domain.member.entity.Member;

public class LikeTestFixture {

    public static TestLike create(Member member) {
        TestLike like = new TestLike();
        like.assignMember(member);
        return like;
    }

    public static class TestLike extends BaseLike {
    }
}
