package com.mobble.mobbleserver.refactor.like.baseLike.entity;

import com.mobble.mobbleserver.domain.like.baseLike.BaseLike;
import com.mobble.mobbleserver.domain.member.Member;

public class LikeTestFixture extends BaseLike {

    public static LikeTestFixture create(Member member) {
        LikeTestFixture like = new LikeTestFixture();
        like.assignMember(member);
        return like;
    }

    @Override
    public Long getTargetId() {
        return 1L;
    }
}
