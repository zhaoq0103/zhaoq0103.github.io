package com.ai.x.mapper;



import com.ai.x.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    @Select("SELECT * FROM t_users WHERE id = #{id}")
    User getById(@Param("id") long id);

    @Select("SELECT * FROM t_users WHERE email = #{email}")
    User getByEmail(@Param("email") String email);

    @Select("SELECT * FROM t_users LIMIT #{offset}, #{maxResults}")
    List<User> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO t_users (email, password, name, createdAt, updateAt) VALUES (#{user.email}, #{user.password}, #{user.name},#{user.createdAt},#{user.updateAt})")
//    @Insert("INSERT INTO t_users (email, password, name) VALUES (#{user.email}, #{user.password}, #{user.name})")
    void insert(@Param("user") User user);

    @Update("UPDATE t_users SET name = #{user.name}, updateAt = #{user.updateAt} WHERE id = #{user.id}")
    void update(@Param("user") User user);

    @Delete("DELETE FROM t_users WHERE id = #{id}")
    void deleteById(@Param("id") long id);
}




