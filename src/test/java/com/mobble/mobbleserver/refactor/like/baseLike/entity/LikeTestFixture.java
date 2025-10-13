package com.mobble.mobbleserver.refactor.like.baseLike.entity;

import com.mobble.mobbleserver.refactor.member.entity.Member;

public class LikeTestFixture extends BaseLike{

    public static LikeTestFixture create(Member member) {
        LikeTestFixture like = new LikeTestFixture();
        like.assignMember(member);
        return like;
    }
}
