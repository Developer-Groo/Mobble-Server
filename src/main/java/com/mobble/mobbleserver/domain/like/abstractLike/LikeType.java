package com.mobble.mobbleserver.domain.like.abstractLike;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeType {
    CLUB,
    ARTICLE,
    COMMENT
}
