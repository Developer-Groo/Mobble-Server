package com.mobble.mobbleserver.domain.member.scheduler;

import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberDeleteSchedulerTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberDeleteScheduler memberDeleteScheduler;

    private static final LocalDateTime NOW = LocalDateTime.of(2025, 9, 26, 18, 0);

    private Member withdrewMember;

    @BeforeEach
    void setUp() {
        withdrewMember = mock(Member.class);
    }

    @Test
    @DisplayName("삭제할 회원이 없는 경우 deleteAll 호출하지 않음")
    void success_when_no_withdrew_members() {
        // given
        given(memberRepository.findAllByIsDeletedTrueAndDeletedAtBefore(any(LocalDateTime.class))).willReturn(List.of());

        // when
        memberDeleteScheduler.deleteWithdrewMembers();

        // then
        verify(memberRepository, times(1))
                .findAllByIsDeletedTrueAndDeletedAtBefore(any(LocalDateTime.class));
        verify(memberRepository, never()).deleteAll(any());
    }
}
