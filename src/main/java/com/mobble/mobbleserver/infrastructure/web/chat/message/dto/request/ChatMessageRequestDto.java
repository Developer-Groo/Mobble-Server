package com.mobble.mobbleserver.infrastructure.web.chat.message.dto.request;

import com.mobble.mobbleserver.application.chat.message.command.SendMessageCommand;
import com.mobble.mobbleserver.domain.chat.message.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ChatMessageRequestDto(
        @NotBlank(message = "content must not be blank")
        String content,

        @NotNull(message = "type must not be null")
        MessageType type,

        @Size(max = 20, message = "mentions member ids must be 20 items or fewer")
        List<
                @NotNull(message = "mentionedMemberIds items must not be null")
                @Positive(message = "mentionedMemberIds items must be positive")
                Long
        > mentionedMemberIds
) {

    public SendMessageCommand toCommand(Long chatRoomId, Long memberId) {
        return SendMessageCommand.create(
                chatRoomId,
                memberId,
                content,
                type,
                mentionedMemberIds
        );
    }
}
