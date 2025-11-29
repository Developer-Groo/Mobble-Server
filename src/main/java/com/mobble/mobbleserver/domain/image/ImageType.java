package com.mobble.mobbleserver.domain.image;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageType {
    MEMBER_PROFILE("members/profile/"),
    CLUB_MAIN("clubs/main/"),
    MEETING_MAIN("meetings/main/"),
    ARTICLE_CONTENT("articles/content/"),;

    private final String path;
}
