package com.mobble.mobbleserver.domain.article.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArticleType {
    NOTICE("공지"),
    REVIEW("모임 후기"),
    GREETING("가입 인사"),
    FREE("자유");

    private final String displayName;

}
