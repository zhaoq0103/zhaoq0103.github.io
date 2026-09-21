package com.ai.x.Messaging;

import com.ai.x.message.AbstractMessage;

import java.util.List;

@FunctionalInterface
public interface BatchMessageHandler<T extends AbstractMessage> {
    void processMessages(List<T> messages);
}
