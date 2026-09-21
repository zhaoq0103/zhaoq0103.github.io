package com.ai.x.message;

import java.io.Serializable;

public class AbstractMessage implements Serializable {
    /**
     * Reference id, or null if not set.
     */
    public String refId = null;

    /**
     * Message created at.
     */
    public long createdAt;
}
