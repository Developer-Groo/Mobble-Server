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
