package com.ai.x.message;

import com.ai.x.model.quotation.TickEntity;

import java.util.List;

public class TickMessage extends AbstractMessage{
    public long sequenceId;

    public List<TickEntity> ticks;
}
