package com.ai.x.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.annotation.Nullable;

/**
 * how to use?
 * @param <T>
 */
@FunctionalInterface
public interface ResultSetExtractor<T> {

    @Nullable
//    T extractData(ResultSet rs) throws SQLException;
    T ho2Use(ResultSet rs) throws SQLException;

}
