package com.mobble.mobbleserver.domain.like.baseLike.service;

import com.mobble.mobbleserver.domain.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;

public interface LikeQueryService {

    LikeType getType();

    default Object getLikedMemberList(Long targetId){
        throw new DomainException(LikeErrorCode.NOT_SUPPORTED_TYPE);
    }
}
