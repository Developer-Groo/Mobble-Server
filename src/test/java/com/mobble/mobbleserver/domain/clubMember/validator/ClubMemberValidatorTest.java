package com.mobble.mobbleserver.domain.clubMember.validator;

import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.repository.ClubMemberRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberValidationErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubMemberValidatorTest {

    @Mock
    private ClubMemberRepository clubMemberRepository;

    @InjectMocks
    private ClubMemberValidator clubMemberValidator;

    private static final Long CLUB_ID = 1L;
    private static final Long MEMBER_ID = 2L;

    @Nested
    @DisplayName("findClubMemberByClubIdAndMemberIdOrThrow")
    class FindByClubAndMember {

        @Test
        @DisplayName("성공 - 존재하면 반환")
        void success_when_exists() {
            // given
            ClubMember clubMember = mock(ClubMember.class);
            given(clubMemberRepository.findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID))
                    .willReturn(Optional.of(clubMember));

            // when
            ClubMember result = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID);

            // then
            assertThat(result).isSameAs(clubMember);
            verify(clubMemberRepository, times(1))
                    .findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID);
        }

        @Test
        @DisplayName("실패 - 존재하지 않으면 예외(CLUB_MEMBER_NOT_FOUND)")
        void fail_when_not_exists() {
            // given
            given(clubMemberRepository.findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() ->
                    clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)
            )
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubMemberValidationErrorCode.CLUB_MEMBER_NOT_FOUND.message());

            verify(clubMemberRepository, times(1))
                    .findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID);
        }
    }

    @Nested
    @DisplayName("findAllClubMemberByMemberId")
    class FindAllByMemberId {

        @Test
        @DisplayName("성공 - 멤버의 모든 클럽멤버 반환")
        void success_return_list() {
            // given
            ClubMember clubMember1 = mock(ClubMember.class);
            ClubMember clubMember2 = mock(ClubMember.class);
            given(clubMemberRepository.findAllClubMemberByMemberId(MEMBER_ID))
                    .willReturn(List.of(clubMember1, clubMember2));

            // when
            List<ClubMember> list = clubMemberValidator.findAllClubMemberByMemberId(MEMBER_ID);

            // then
            assertThat(list).hasSize(2)
                    .containsExactly(clubMember1, clubMember2);
            verify(clubMemberRepository, times(1))
                    .findAllClubMemberByMemberId(MEMBER_ID);
        }
