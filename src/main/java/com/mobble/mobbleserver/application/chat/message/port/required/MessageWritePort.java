package com.mobble.mobbleserver.application.chat.message.port.required;

import com.mobble.mobbleserver.domain.chat.message.ChatMessage;

public interface MessageWritePort {

    ChatMessage save(ChatMessage chatMessage);
}
