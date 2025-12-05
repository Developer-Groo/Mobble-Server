package com.mobble.mobbleserver.infrastructure.web.meeting.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mobble.mobbleserver.application.meeting.command.CreateMeetingCommand;
import com.mobble.mobbleserver.application.meeting.command.UpdateMeetingCommand;
import com.mobble.mobbleserver.domain.meeting.MeetingType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record MeetingRequestDto(
        @NotBlank(message = "title must not be blank")
        @Size(max = 50, message = "title must be 50 characters or fewer")
        String title,

        @Positive(message = "main image id must be positive")
        Long mainImageId,

        @NotNull(message = "schedule must not be null")
        @FutureOrPresent(message = "schedule must not be in the past")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime schedule,

        @NotBlank(message = "location must not be blank")
        @Size(max = 50, message = "location must be 50 characters or fewer")
        String location,

        @NotBlank(message = "cost must not be blank")
        @Size(max = 50, message = "cost must be 10 characters or fewer")
        String cost,

        @NotNull(message = "memberLimit must not be null")
        @Min(value = 1, message = "memberLimit must be at least 1")
        Integer memberLimit,

        @NotNull(message = "type must not be null")
        MeetingType type
) {

    public CreateMeetingCommand toCreateCommand(Long memberId, Long clubId) {
        return CreateMeetingCommand.create(
                memberId,
                clubId,
                title,
                mainImageId,
                schedule,
                location,
                cost,
                memberLimit,
                type
        );
    }

    public UpdateMeetingCommand toUpdateCommand(Long memberId, Long clubId, Long meetingId) {
        return UpdateMeetingCommand.create(
                memberId,
                clubId,
                meetingId,
                title,
                mainImageId,
                schedule,
                location,
                cost,
                memberLimit,
                type
        );
    }
}
