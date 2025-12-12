package com.mobble.mobbleserver.application.chat.message.port.provided;

import com.mobble.mobbleserver.application.chat.message.command.SendMessageCommand;

public interface SendMessagePort {

    void send(SendMessageCommand command);
}
