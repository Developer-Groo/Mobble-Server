package com.mobble.mobbleserver.domain.meeting.validator;

import com.mobble.mobbleserver.domain.meeting.entity.Meeting;
import com.mobble.mobbleserver.domain.meeting.repository.MeetingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
