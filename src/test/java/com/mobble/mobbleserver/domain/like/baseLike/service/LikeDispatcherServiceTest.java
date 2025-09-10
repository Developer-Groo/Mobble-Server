package com.mobble.mobbleserver.domain.like.baseLike.service;

import com.mobble.mobbleserver.domain.like.articleLike.service.ArticleLikeQueryService;
import com.mobble.mobbleserver.domain.like.articleLike.service.ArticleLikeService;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LikeDispatcherServiceTest {

    private MemberValidator mockMemberValidator;
    private ArticleLikeService mockArticleLikeService;
    private ArticleLikeQueryService mockArticleLikeQueryService;

    private LikeDispatcherService likeDispatcherService;

    private static final Long TARGET_ID = 1L;
    private static final Long MEMBER_ID = 10L;

    private Member mockMember;

    @BeforeEach
    void setUp() {
        mockMemberValidator = mock(MemberValidator.class);
        mockArticleLikeService = mock(ArticleLikeService.class);
        mockArticleLikeQueryService = mock(ArticleLikeQueryService.class);

        likeDispatcherService = new LikeDispatcherService(
                mockMemberValidator,
                List.of(mockArticleLikeService),
                List.of(mockArticleLikeQueryService)
        );

        mockMember = mock(Member.class);
    }
}
