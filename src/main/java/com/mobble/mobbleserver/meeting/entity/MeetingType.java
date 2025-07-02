package com.mobble.mobbleserver.meeting.entity;

import lombok.Getter;

@Getter
public enum MeetingType {
    REGULAR_MEETING("정기 모임"),
    IMPROMPTU_MEETING("번개 모임");

    private final String displayName;

    MeetingType(String displayName) {
        this.displayName = displayName;
    }
}
