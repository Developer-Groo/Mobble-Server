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

        @Test
        @DisplayName("정원 초과면 CLUB_IS_FULL")
        void full_throws() {
            given(mockClub.getId()).willReturn(CLUB_ID);
            given(mockClub.getHeadCount()).willReturn(3);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
            given(clubMemberRepository.findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID)).willReturn(Optional.empty());
            given(clubMemberRepository.countByClubIdAndJoinStatus(CLUB_ID, JoinStatus.APPROVED)).willReturn(3L);

            assertThatThrownBy(() -> clubMemberService.joinClub(MEMBER_ID, CLUB_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubMemberErrorCode.CLUB_IS_FULL.message());
        }
    }

    @Nested
    @DisplayName("withdrawClub")
    class WithdrawClub {

        @Test
        @DisplayName("가입되어 있으면 WITHDRAWN")
        void withdraw_success() {
            ClubMember clubMember = ClubMember.createClubMember(mockMember, mockClub, ClubMemberRole.MEMBER,
                    JoinStatus.APPROVED);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
            given(clubMemberRepository.findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID)).willReturn(Optional.of(clubMember));
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, MEMBER_ID)).willReturn(clubMember);

            clubMemberService.withdrawClub(MEMBER_ID, CLUB_ID);

            assertThat(clubMember.getJoinStatus()).isEqualTo(JoinStatus.WITHDRAWN);
        }

        @Test
        @DisplayName("가입되어 있지 않으면 NOT_JOINED_CLUB")
        void withdraw_notJoined_throws() {
            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(MEMBER_ID)).willReturn(mockMember);
            given(clubMemberRepository.findClubMemberByClubIdAndMemberId(CLUB_ID, MEMBER_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> clubMemberService.withdrawClub(MEMBER_ID, CLUB_ID))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubMemberErrorCode.NOT_JOINED_CLUB.message());
        }
    }

    @Nested
    @DisplayName("updateClubMemberRole")
    class UpdateClubMemberRoleTest {

        @Test
        @DisplayName("리더가 권한 변경 → 토큰 재발급")
        void leader_changes_role_and_reissues_token() {
            given(mockClub.getId()).willReturn(CLUB_ID);
            given(leader.getId()).willReturn(LEADER_ID);
            given(target.getId()).willReturn(TARGET_ID);

            ClubMember leaderCM = ClubMember.createClubMember(leader, mockClub, ClubMemberRole.LEADER, JoinStatus.APPROVED);
            ClubMember targetCM = ClubMember.createClubMember(target, mockClub, ClubMemberRole.MEMBER, JoinStatus.APPROVED);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(TARGET_ID)).willReturn(target);
            given(memberValidator.findMemberByMemberIdOrThrow(LEADER_ID)).willReturn(leader);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, LEADER_ID)).willReturn(leaderCM);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, TARGET_ID)).willReturn(targetCM);

            List<ClubMemberRole> roles = List.of(ClubMemberRole.LEADER, ClubMemberRole.MANAGER);
            given(clubMemberRepository.findDistinctRolesByMemberIdAndRoleIn(eq(TARGET_ID), anyList())).willReturn(roles);
            given(tokenProvider.createJwtToken(TARGET_ID, roles)).willReturn("jwt-token");

            UpdateClubMemberRoleDto dto = new UpdateClubMemberRoleDto(TARGET_ID, ClubMemberRole.MANAGER);

            ClubMemberRoleUpdateResultDto result = clubMemberService.updateClubMemberRole(CLUB_ID, LEADER_ID, dto);

            assertThat(result).isNotNull();
            assertThat(targetCM.getClubMemberRole()).isEqualTo(ClubMemberRole.MANAGER);
            verify(tokenProvider).createJwtToken(TARGET_ID, roles);
        }

        @Test
        @DisplayName("자기 자신의 권한은 변경 불가")
        void cannot_change_own_role() {
            given(leader.getId()).willReturn(LEADER_ID);

            ClubMember leaderCM = ClubMember.createClubMember(
                    leader, mockClub, ClubMemberRole.LEADER, JoinStatus.APPROVED);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(LEADER_ID)).willReturn(leader);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, LEADER_ID))
                    .willReturn(leaderCM);

            UpdateClubMemberRoleDto dto =
                    new UpdateClubMemberRoleDto(LEADER_ID, ClubMemberRole.MANAGER);

            assertThatThrownBy(() -> clubMemberService.updateClubMemberRole(CLUB_ID, LEADER_ID, dto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubMemberErrorCode.CANNOT_CHANGE_OWN_ROLE.message());

            verifyNoInteractions(tokenProvider);
        }

        @Test
        @DisplayName("리더가 아니면 NO_PERMISSION")
        void non_leader_no_permission() {
            given(leader.getId()).willReturn(LEADER_ID);

            ClubMember managerCM = ClubMember.createClubMember(
                    leader, mockClub, ClubMemberRole.MANAGER, JoinStatus.APPROVED);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(LEADER_ID)).willReturn(leader);
            given(memberValidator.findMemberByMemberIdOrThrow(TARGET_ID)).willReturn(target);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, LEADER_ID))
                    .willReturn(managerCM);

            UpdateClubMemberRoleDto dto = new UpdateClubMemberRoleDto(TARGET_ID, ClubMemberRole.MEMBER);

            assertThatThrownBy(() -> clubMemberService.updateClubMemberRole(CLUB_ID, LEADER_ID, dto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubMemberErrorCode.NO_PERMISSION.message());

            verify(clubMemberValidator, never())
                    .findClubMemberByClubIdAndMemberIdOrThrow(eq(CLUB_ID), eq(TARGET_ID));
            verifyNoInteractions(tokenProvider);
        }

    }

    @Nested
    @DisplayName("updateClubMemberJoinStatus")
    class UpdateJoinStatus {

        @Test
        @DisplayName("리더가 WAITING → APPROVED (정원 미초과)")
        void approve_success() {
            given(mockClub.getId()).willReturn(CLUB_ID);
            given(leader.getId()).willReturn(LEADER_ID);
            given(target.getId()).willReturn(TARGET_ID);
            given(mockClub.getHeadCount()).willReturn(2);

            ClubMember leaderCM = ClubMember.createClubMember(leader, mockClub, ClubMemberRole.LEADER, JoinStatus.APPROVED);
            ClubMember targetCM = ClubMember.createClubMember(target, mockClub, ClubMemberRole.MEMBER, JoinStatus.WAITING);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(TARGET_ID)).willReturn(target);
            given(memberValidator.findMemberByMemberIdOrThrow(LEADER_ID)).willReturn(leader);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, LEADER_ID)).willReturn(leaderCM);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, TARGET_ID)).willReturn(targetCM);

            given(clubMemberRepository.countByClubIdAndJoinStatus(CLUB_ID, JoinStatus.APPROVED)).willReturn(1L);

            UpdateClubMemberStatusDto dto = new UpdateClubMemberStatusDto(TARGET_ID, JoinStatus.APPROVED);

            ClubMemberUpsertResponseDto res = clubMemberService.updateClubMemberJoinStatus(CLUB_ID, LEADER_ID, dto);

            assertThat(res).isNotNull();
            assertThat(targetCM.getJoinStatus()).isEqualTo(JoinStatus.APPROVED);
        }

        @Test
        @DisplayName("APPROVED로 변경하려는데 정원 초과 → CLUB_IS_FULL")
        void approve_full_throws() {
            given(mockClub.getId()).willReturn(CLUB_ID);
            given(leader.getId()).willReturn(LEADER_ID);
            given(target.getId()).willReturn(TARGET_ID);
            given(mockClub.getHeadCount()).willReturn(1);

            ClubMember leaderCM = ClubMember.createClubMember(leader, mockClub, ClubMemberRole.LEADER, JoinStatus.APPROVED);
            ClubMember targetCM = ClubMember.createClubMember(target, mockClub, ClubMemberRole.MEMBER, JoinStatus.WAITING);

            given(clubValidator.findClubByClubIdOrThrow(CLUB_ID)).willReturn(mockClub);
            given(memberValidator.findMemberByMemberIdOrThrow(TARGET_ID)).willReturn(target);
            given(memberValidator.findMemberByMemberIdOrThrow(LEADER_ID)).willReturn(leader);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, LEADER_ID)).willReturn(leaderCM);
            given(clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(CLUB_ID, TARGET_ID)).willReturn(targetCM);

            given(clubMemberRepository.countByClubIdAndJoinStatus(CLUB_ID, JoinStatus.APPROVED)).willReturn(1L);

            UpdateClubMemberStatusDto dto = new UpdateClubMemberStatusDto(TARGET_ID, JoinStatus.APPROVED);

            assertThatThrownBy(() -> clubMemberService.updateClubMemberJoinStatus(CLUB_ID, LEADER_ID, dto))
                    .isInstanceOf(DomainException.class)
                    .hasMessage(ClubMemberErrorCode.CLUB_IS_FULL.message());
        }
