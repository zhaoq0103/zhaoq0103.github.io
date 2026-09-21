package com.ai.x.jdbc.with.tx;

import com.ai.x.annotation.Autowired;
import com.ai.x.annotation.Component;
import com.ai.x.annotation.Transactional;
import com.ai.x.jdbc.JdbcTemplate;
import com.ai.x.jdbc.JdbcTest;

import java.util.List;


@Component
@Transactional
public class AddressService {

    @Autowired
    UserService userService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addAddress(Address... addresses) {
        for (Address address : addresses) {
            // check if userId is exist:
            userService.getUser(address.userId);
            jdbcTemplate.update(JdbcTest.INSERT_ADDRESS, address.userId, address.address, address.zip);
        }
    }

    public List<Address> getAddresses(int userId) {
        return jdbcTemplate.queryForList(JdbcTest.SELECT_ADDRESS_BY_USERID, Address.class, userId);
    }

    public void deleteAddress(int userId) {
        jdbcTemplate.update(JdbcTest.DELETE_ADDRESS_BY_USERID, userId);
        if (userId == 1) {
            throw new RuntimeException("Rollback delete for user id = 1");
        }
    }
}
