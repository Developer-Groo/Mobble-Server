package com.mobble.mobbleserver.domain.meetingMember.validation;

import com.mobble.mobbleserver.domain.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.domain.meetingMember.repository.MeetingMemberRepository;
import com.mobble.mobbleserver.domain.meetingMember.validator.MeetingMemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MeetingMemberValidatorTest {

    @Mock
    private MeetingMemberRepository meetingMemberRepository;

    @InjectMocks
    private MeetingMemberValidator meetingMemberValidator;

    private static final Long MEETING_ID = 1L;
    private static final Long MEMBER_ID = 2L;

    private MeetingMember meetingMember;

    @BeforeEach
    void setUp() {
        meetingMember = mock(MeetingMember.class);
    }

    @Test
    @DisplayName("meetingId, memberId 로 MeetingMember 조회 성공")
    void success_when_find_meeting_member_by_meeting_id_member_id_with_optional() {
        // given
        given(meetingMemberRepository.findMeetingMemberByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID)).willReturn(Optional.of(meetingMember));

        // when
        Optional<MeetingMember> result = meetingMemberValidator.findMeetingByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(meetingMember);
        verify(meetingMemberRepository).findMeetingMemberByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID);
    }
}
