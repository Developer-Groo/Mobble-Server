package com.mobble.mobbleserver.refactor.meetingMember.validation;

import com.mobble.mobbleserver.refactor.meetingMember.entity.MeetingMember;
import com.mobble.mobbleserver.refactor.meetingMember.repository.MeetingMemberRepository;
import com.mobble.mobbleserver.refactor.meetingMember.validator.MeetingMemberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
    @DisplayName("meetingId, memberId로 MeetingMember 조회 성공")
    void success_when_find_meeting_member_by_meeting_id_member_id() {
        // given
        given(meetingMemberRepository.findMeetingMemberByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID)).willReturn(Optional.of(meetingMember));

        // when
        Optional<MeetingMember> result = meetingMemberValidator.findMeetingByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(meetingMember);
        verify(meetingMemberRepository).findMeetingMemberByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID);
    }

    @Test
    @DisplayName("meetingId, memberId로 MeetingMember 조회 실패 시 Optional.empty 반환")
    void fail_when_meeting_member_not_found() {
        // given
        given(meetingMemberRepository.findMeetingMemberByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID)).willReturn(Optional.empty());

        // when
        Optional<MeetingMember> result = meetingMemberValidator.findMeetingByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID);

        // then
        assertThat(result).isNotPresent();
        verify(meetingMemberRepository).findMeetingMemberByMeetingIdAndMemberId(MEETING_ID, MEMBER_ID);
    }

    @Test
    @DisplayName("meetingId로 MeetingMember 리스트 조회 성공")
    void success_when_find_meeting_member_list() {
        // given
        List<MeetingMember> meetingMembers = List.of(meetingMember);
        given(meetingMemberRepository.findByMeetingId(MEETING_ID)).willReturn(meetingMembers);

        // when
        List<MeetingMember> result = meetingMemberValidator.findByMeetingId(MEETING_ID);

        // then
        assertThat(result).hasSize(1).containsExactly(meetingMember);
        verify(meetingMemberRepository).findByMeetingId(MEETING_ID);
    }

    @Test
    @DisplayName("meetingId로 조회된 MeetingMember 가 없으면 빈 리스트 반환")
    void success_return_empty_list_when_no_attendees() {
        // given
        given(meetingMemberRepository.findByMeetingId(MEETING_ID)).willReturn(List.of());

        // when
        List<MeetingMember> result = meetingMemberValidator.findByMeetingId(MEETING_ID);

        // then
        assertThat(result).isEmpty();
        verify(meetingMemberRepository).findByMeetingId(MEETING_ID);
    }
}
