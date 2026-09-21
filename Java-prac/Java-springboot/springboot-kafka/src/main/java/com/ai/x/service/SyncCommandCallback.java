package com.ai.x.service;

import io.lettuce.core.api.sync.RedisCommands;

public interface SyncCommandCallback<T> {
    T doInConnection(RedisCommands<String, String> commands);
}
