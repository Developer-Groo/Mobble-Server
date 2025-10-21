package com.mobble.mobbleserver.application.like.required;

import com.mobble.mobbleserver.infrastructure.web.like.dto.response.LikeMemberListResponseDto;

public interface LikeMemberListReadPort {

    LikeMemberListResponseDto getLikedMembers(Long articleId);
}
