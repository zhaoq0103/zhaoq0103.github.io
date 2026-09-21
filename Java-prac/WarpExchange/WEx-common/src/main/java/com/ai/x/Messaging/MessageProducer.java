package com.ai.x.Messaging;

import com.ai.x.message.AbstractMessage;

import java.util.List;

public interface MessageProducer<T extends AbstractMessage> {
    void sendMessage(T message);

    default void sendMessages(List<T> messages) {
        for (T message : messages) {
            sendMessage(message);
        }
    }
}
