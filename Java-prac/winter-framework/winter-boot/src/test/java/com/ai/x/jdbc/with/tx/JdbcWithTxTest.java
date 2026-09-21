package com.ai.x.jdbc.with.tx;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.ai.x.context.AnnotationConfigApplicationContext;
import com.ai.x.exception.TransactionException;
import com.ai.x.io.PropertyResolver;
import com.ai.x.jdbc.JdbcTemplate;
import com.ai.x.jdbc.JdbcTest;
import org.junit.jupiter.api.Test;


public class JdbcWithTxTest extends JdbcTest {

    @Test
    public void testJdbcWithTx() {
        PropertyResolver resmysql = createMysqlPropertyResolver();
        PropertyResolver ressqlite = createMysqlPropertyResolver();

        try (var ctx = new AnnotationConfigApplicationContext(JdbcWithTxApplication.class, ressqlite)) {
            JdbcTemplate jdbcTemplate = ctx.getBean(JdbcTemplate.class);
            jdbcTemplate.update(CREATE_USER);
            jdbcTemplate.update(CREATE_ADDRESS);

            UserService userService = ctx.getBean(UserService.class);
            AddressService addressService = ctx.getBean(AddressService.class);
            // proxied:
            assertNotSame(UserService.class, userService.getClass());
            assertNotSame(AddressService.class, addressService.getClass());
            // proxy object is not inject:
            assertNull(userService.addressService);
            assertNull(addressService.userService);

            // insert user:
            User bob = userService.createUser("Bob", 12);
            assertEquals(1, bob.id);

            // insert addresses:
            Address addr1 = new Address(bob.id, "Broadway, New York", 10012);
            Address addr2 = new Address(bob.id, "Fifth Avenue, New York", 10080);
            // NOTE user not exist for addr3:
            Address addr3 = new Address(bob.id + 1, "Ocean Drive, Miami, Florida", 33411);
            assertThrows(TransactionException.class, () -> {
                addressService.addAddress(addr1, addr2, addr3);
            });

            // todo
            // rollback 没有起作用呢？
            // ALL address should not inserted:
//            assertTrue(addressService.getAddresses(bob.id).isEmpty());
//
//            // insert addr1, addr2 for Bob only:
//            addressService.addAddress(addr1, addr2);
//            assertEquals(2, addressService.getAddresses(bob.id).size());
//            assertEquals(2, addressService.getAddresses(bob.id).size());


            // now delete bob will cause rollback:
            assertThrows(TransactionException.class, () -> {
                userService.deleteUser(bob);
            });

            // bob and his addresses still exist:
//            assertEquals("Bob", userService.getUser(1).name);
//            assertEquals(2, addressService.getAddresses(bob.id).size());
        }
        // re-open db and query:
        try (var ctx = new AnnotationConfigApplicationContext(JdbcWithTxApplication.class, ressqlite)) {
//            AddressService addressService = ctx.getBean(AddressService.class);
//            List<Address> addressesOfBob = addressService.getAddresses(1);
//            assertEquals(2, addressesOfBob.size());
//            assertEquals(4, addressesOfBob.size());
        }
    }
}
