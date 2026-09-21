package com.ai.x.support;

import com.ai.x.db.DbTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDbSupport extends LoggerSupport {
    @Autowired
    protected DbTemplate db;
}
