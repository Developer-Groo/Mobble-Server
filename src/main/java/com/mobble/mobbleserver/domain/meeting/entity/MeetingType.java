package com.mobble.mobbleserver.domain.meeting.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MeetingType {
    REGULAR_MEETING("정기 모임"),
    IMPROMPTU_MEETING("번개 모임");

    private final String displayName;
}
