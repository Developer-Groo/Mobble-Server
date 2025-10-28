package com.mobble.mobbleserver.refactor.like.baseLike.entity;

import com.mobble.mobbleserver.domain.like.abstractLike.AbstractLike;
import com.mobble.mobbleserver.domain.member.Member;

public class LikeTestFixture extends AbstractLike {

    public static LikeTestFixture create(Member member) {
        LikeTestFixture like = new LikeTestFixture();
        like.assignMember(member);
        return like;
    }
}
