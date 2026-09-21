package com.ai.x.jdbc.tx;

import java.sql.Connection;

public class TransactionStatus {

    final Connection connection;

    public TransactionStatus(Connection connection) {
        this.connection = connection;
    }
}
