package com.mobble.mobbleserver.domain.like.clubLike.service;

import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.validator.ClubValidator;
import com.mobble.mobbleserver.domain.like.clubLike.entity.ClubLike;
import com.mobble.mobbleserver.domain.like.clubLike.repository.ClubLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubValidationErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubLikeServiceTest {

    @Mock
    ClubLikeRepository clubLikeRepository;

    @Mock
    ClubValidator clubValidator;

    @InjectMocks
    ClubLikeService clubLikeService;

    private static final Long CLUB_ID = 1L;
    private static final Long MEMBER_ID = 2L;

    private Club mockClub;
    private Member mockMember;
    private ClubLike mockClubLike;

    @BeforeEach
    void setUp() {
        mockClub = mock(Club.class);
        mockMember = mock(Member.class);
        mockClubLike = mock(ClubLike.class);
    }

    @DisplayName("좋아요가 없으면 생성")
    @Test
    void success_when_not_liked() {
        // given
        given(mockClub.getId()).willReturn(CLUB_ID);
        given(mockMember.getId()).willReturn(MEMBER_ID);
        given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
        given(clubLikeRepository.findLikedByClubIdAndMemberId(CLUB_ID, MEMBER_ID)).willReturn(Optional.empty());
        given(clubLikeRepository.save(any(ClubLike.class))).willReturn(mockClubLike);

        // when
        boolean result = clubLikeService.toggleLike(CLUB_ID, mockMember).isLiked();

        // then
        assertThat(result).isTrue();
        verify(clubLikeRepository).save(any(ClubLike.class));
    }

    @DisplayName("좋아요가 있으면 삭제")
    @Test
    void success_when_already_liked() {
        // given
        given(mockClub.getId()).willReturn(CLUB_ID);
        given(mockMember.getId()).willReturn(MEMBER_ID);
        given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
        given(clubLikeRepository.findLikedByClubIdAndMemberId(CLUB_ID, MEMBER_ID)).willReturn(Optional.of(mockClubLike));

        // when
        boolean result = clubLikeService.toggleLike(CLUB_ID, mockMember).isLiked();

        // then
        assertThat(result).isFalse();
        verify(clubLikeRepository).delete(mockClubLike);
        verify(clubLikeRepository, never()).save(any());
    }

    @DisplayName("좋아요할 클럽이 없으면 예외 발생")
    @Test
    void fail_when_club_not_found() {
        // given
        given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willThrow(new DomainException(ClubErrorCode.NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> clubLikeService.toggleLike(CLUB_ID, mockMember))
                .isInstanceOf(DomainException.class)
                .hasMessage(ClubErrorCode.NOT_FOUND.message());
    }
}
