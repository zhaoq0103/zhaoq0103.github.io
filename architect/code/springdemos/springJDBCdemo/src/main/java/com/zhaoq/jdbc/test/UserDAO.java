package com.zhaoq.jdbc.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//好像用这个注解还不行，没有想到原因，以后再理解
//@Component(value = "userDao")
public class UserDAO extends JdbcDaoSupport {

    public void add(User user){
        String sql = "insert  into user values(?,?)";
        this.getJdbcTemplate().update(sql,user.getId(), user.getName());
    }


    public void delete(User user){
        String sql = "delete from user  where id = ?";
        this.getJdbcTemplate().update(sql,user.getId());
    }


    public void find(){

    }

    public void update(User user){

        String sql = "update user set name=? where id=?";
        this.getJdbcTemplate().update(sql,user.getName(),user.getId());

    }

    public int findCount(){
        String sql = "select count(*) from user";
        return this.getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    public User findById(int id){
        String sql = "select * from user where id = ?";
        User user = (User) this.getJdbcTemplate().queryForObject(sql, new UserMapper(), id);
        return user;
    }

    public List<User> findAll(){
        String sql = "select * from user";
        return (List<User>) this.getJdbcTemplate().query(sql, new UserMapper());
    }

    private class UserMapper  implements RowMapper {

        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User() ;
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            user.setId(id);
            user.setName(name);

            return user;
        }
    }
}
