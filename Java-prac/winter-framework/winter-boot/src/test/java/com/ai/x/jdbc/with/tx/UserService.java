package com.ai.x.jdbc.with.tx;


import com.ai.x.annotation.Autowired;
import com.ai.x.annotation.Component;
import com.ai.x.annotation.Transactional;
import com.ai.x.jdbc.JdbcTemplate;
import com.ai.x.jdbc.JdbcTest;

@Component
@Transactional
public class UserService {

    @Autowired
    AddressService addressService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public User createUser(String name, int age) {
        Number id = jdbcTemplate.updateAndReturnGeneratedKey(JdbcTest.INSERT_USER, name, age);
        User user = new User();
        user.id = id.intValue();
        user.name = name;
        user.theAge = age;
        return user;
    }

    public User getUser(int userId) {
        return jdbcTemplate.queryForObject(JdbcTest.SELECT_USER, User.class, userId);
    }

    public void updateUser(User user) {
        jdbcTemplate.update(JdbcTest.UPDATE_USER, user.name, user.theAge, user.id);
    }

    public void deleteUser(User user) {
        jdbcTemplate.update(JdbcTest.DELETE_USER, user.id);
        addressService.deleteAddress(user.id);
    }
}
