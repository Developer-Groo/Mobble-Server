package com.mobble.mobbleserver.domain.meeting.validator;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.repository.MeetingRepository;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.meeting.MeetingErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeetingValidatorTest {

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private MeetingValidator meetingValidator;

    private static final Long MEETING_ID = 1L;
    private static final Long CLUB_ID = 2L;

    @Test
    @DisplayName("Meeting ID로 조회 성공")
    void success_when_find_meeting_or_throw() {
        // given
        Meeting mockMeeting = mock(Meeting.class);

        when(meetingRepository.findById(MEETING_ID)).thenReturn(Optional.of(mockMeeting));

        // when
        Meeting result = meetingValidator.findMeetingByMeetingIdOrThrow(MEETING_ID);

        // then
        assertThat(result).isEqualTo(mockMeeting);
    }

    @Test
    @DisplayName("Meeting ID로 조회 실패 시 예외 발생")
    void fail_when_find_meeting_or_throw() {
        // given
        when(meetingRepository.findById(MEETING_ID)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> meetingValidator.findMeetingByMeetingIdOrThrow(MEETING_ID))
                .isInstanceOf(DomainException.class)
                .hasMessage(MeetingErrorCode.NOT_FOUND_MEETING.message());
    }

    @Test
    @DisplayName("Club ID로 Meeting 리스트 조회 성공")
    void success_when_find_meetings_by_club_id() {
        // given
        Meeting meeting1 = mock(Meeting.class);
        Meeting meeting2 = mock(Meeting.class);
        when(meetingRepository.findByClubMember_Club_Id(CLUB_ID)).thenReturn(List.of(meeting1, meeting2));

        // when
        List<Meeting> result = meetingValidator.findMeetingsByClubId(CLUB_ID);

        // then
        assertThat(result).containsExactly(meeting1, meeting2);
    }

    @Test
    @DisplayName("Club ID로 조회 시 Meeting 이 없는 경우 빈 리스트 반환")
    void success_when_find_meetings_by_club_id_empty_list() {
        // given
        when(meetingRepository.findByClubMember_Club_Id(CLUB_ID)).thenReturn(Collections.emptyList());

        // when
        List<Meeting> result = meetingValidator.findMeetingsByClubId(CLUB_ID);

        // then
        assertThat(result).isEmpty();
    }
}
