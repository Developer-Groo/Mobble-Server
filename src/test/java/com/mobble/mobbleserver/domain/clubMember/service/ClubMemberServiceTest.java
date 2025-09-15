package com.mobble.mobbleserver.domain.clubMember.service;

import com.mobble.mobbleserver.account.jwt.TokenProvider;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.validator.ClubValidator;
import com.mobble.mobbleserver.domain.clubMember.dto.request.UpdateClubMemberRoleDto;
import com.mobble.mobbleserver.domain.clubMember.dto.request.UpdateClubMemberStatusDto;
import com.mobble.mobbleserver.domain.clubMember.dto.response.ClubMemberResponseDto;
import com.mobble.mobbleserver.domain.clubMember.dto.response.ClubMemberRoleUpdateResultDto;
import com.mobble.mobbleserver.domain.clubMember.dto.response.ClubMemberUpsertResponseDto;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.entity.JoinStatus;
import com.mobble.mobbleserver.domain.clubMember.repository.ClubMemberRepository;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubMemberServiceTest {

    @Mock
    private ClubMemberRepository clubMemberRepository;

    @Mock
    private ClubValidator clubValidator;

    @Mock
    private MemberValidator memberValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private ClubMemberService clubMemberService;

    private static final Long CLUB_ID = 1L;
    private static final Long MEMBER_ID = 2L;
    private static final Long LEADER_ID = 3L;
    private static final Long TARGET_ID = 4L;


    private Club mockClub;
    private Member mockMember;
    private Member leader;
    private Member target;

    @BeforeEach
    void setUp() {
        mockClub = mock(Club.class);
        mockMember = mock(Member.class);
        leader = mock(Member.class);
        target = mock(Member.class);
    }
    @Nested
    @DisplayName("joinClub")
    class JoinClub {

        @Test
        @DisplayName("자동가입 클럽이면 APPROVED로 가입")
        void autoJoin_approved() {
            given(mockClub.getId()).willReturn(CLUB_ID);
            given(mockClub.getHeadCount()).willReturn(5);
            given(mockClub.isAutoJoin()).willReturn(true);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
            given(clubMemberRepository.findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID)).willReturn(Optional.empty());
            given(clubMemberRepository.countByClubIdAndJoinStatus(CLUB_ID, JoinStatus.APPROVED)).willReturn(0L);

            given(clubMemberRepository.save(any(ClubMember.class))).willAnswer(inv -> inv.getArgument(0));

            ClubMemberUpsertResponseDto res = clubMemberService.joinClub(MEMBER_ID, CLUB_ID);

            assertThat(res).isNotNull();
            assertThat(res.joinStatus()).isEqualTo(JoinStatus.APPROVED);
            assertThat(res.memberRole()).isEqualTo(ClubMemberRole.MEMBER.getDisplayName());

            verify(clubMemberRepository, times(1)).save(any(ClubMember.class));
        }

        @Test
        @DisplayName("수동승인 클럽이면 WAITING으로 가입")
        void manualJoin_waiting() {
            given(mockClub.getId()).willReturn(CLUB_ID);
            given(mockClub.getHeadCount()).willReturn(10);
            given(mockClub.isAutoJoin()).willReturn(false);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
            given(clubMemberRepository.findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID)).willReturn(Optional.empty());
            given(clubMemberRepository.countByClubIdAndJoinStatus(CLUB_ID, JoinStatus.APPROVED)).willReturn(0L);

            given(clubMemberRepository.save(any(ClubMember.class))).willAnswer(inv -> inv.getArgument(0));

            ClubMemberUpsertResponseDto res = clubMemberService.joinClub(MEMBER_ID, CLUB_ID);

            assertThat(res).isNotNull();
            assertThat(res.joinStatus()).isEqualTo(JoinStatus.WAITING);
            assertThat(res.memberRole()).isEqualTo(ClubMemberRole.MEMBER.getDisplayName());

            verify(clubMemberRepository, times(1)).save(any(ClubMember.class));
        }

        @Test
        @DisplayName("이미 가입되어 있으면 ALREADY_JOINED")
        void alreadyJoined_throws() {
            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
            given(clubMemberRepository.findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID))
                    .willReturn(Optional.of(mock(ClubMember.class)));

            assertThatThrownBy(() -> clubMemberService.joinClub(MEMBER_ID, CLUB_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubMemberErrorCode.ALREADY_JOINED.message());

            verify(clubMemberRepository, never()).save(any());
        }
