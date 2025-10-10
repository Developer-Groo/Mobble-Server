package com.mobble.mobbleserver.domain.like.baseLike.service;

import com.mobble.mobbleserver.domain.like.articleLike.service.ArticleLikeQueryService;
import com.mobble.mobbleserver.domain.like.articleLike.service.ArticleLikeService;
import com.mobble.mobbleserver.domain.like.baseLike.dto.response.LikeMemberListResponseDto;
import com.mobble.mobbleserver.domain.like.baseLike.dto.response.LikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.baseLike.entity.LikeType;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.like.LikeErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
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

        given(mockArticleLikeService.getType()).willReturn(LikeType.ARTICLE);
        given(mockArticleLikeQueryService.getType()).willReturn(LikeType.ARTICLE);


        likeDispatcherService = new LikeDispatcherService(
                mockMemberValidator,
                List.of(mockArticleLikeService),
                List.of(mockArticleLikeQueryService));

        mockMember = mock(Member.class);
    }

    @Nested
    @DisplayName("toggleLike")
    class ToggleLikeTest {

        @Test
        @DisplayName("성공 - ARTICLE 타입만 지원")
        void success_toggleLike_article() {
            // given
            given(mockMemberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
            given(mockArticleLikeService.toggleLike(TARGET_ID, mockMember)).willReturn(new LikeToggleResponseDto(TARGET_ID, true));

            // when
            LikeToggleResponseDto result = likeDispatcherService.toggleLike(LikeType.ARTICLE, TARGET_ID, MEMBER_ID);

            // then
            assertThat(result.isLiked()).isTrue();
            assertThat(result.targetId()).isEqualTo(TARGET_ID);
        }

        @Test
        @DisplayName("실패 - 지원하지 않는 LikeType")
        void fail_toggleLike_invalidType() {
            // when & then
            assertThatThrownBy(() -> likeDispatcherService.toggleLike(LikeType.CLUB, TARGET_ID, MEMBER_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(LikeErrorCode.NOT_SUPPORTED_TYPE.message());
        }
    }

    @Nested
    @DisplayName("getMemberList")
    class GetMemberListTest {

        @Test
        @DisplayName("성공 - ARTICLE 타입만 지원")
        void success_getMemberList_article() {
            // given
            LikeMemberListResponseDto response = new LikeMemberListResponseDto(TARGET_ID, List.of());
            given(mockArticleLikeQueryService.getLikedMemberList(TARGET_ID)).willReturn(response);

            // when
            LikeMemberListResponseDto result = likeDispatcherService.getMemberList(LikeType.ARTICLE, TARGET_ID);

            // then
            assertThat(result).isEqualTo(response);
        }

        @Test
        @DisplayName("실패 - 지원하지 않는 LikeType")
        void fail_getMemberList_invalidType() {
            // when & then
            assertThatThrownBy(() -> likeDispatcherService.getMemberList(LikeType.CLUB, TARGET_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(LikeErrorCode.NOT_SUPPORTED_TYPE.message());
        }
    }
}
