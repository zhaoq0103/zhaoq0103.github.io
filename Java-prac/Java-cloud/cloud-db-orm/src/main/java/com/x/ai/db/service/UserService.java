package com.x.ai.db.service;

import com.x.ai.db.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@Component
public class UserService {

    @Autowired
    SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.REQUIRED)
    public User register(String email, String password, String name) {
        User user = new User();
        // 设置好各个属性:
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        // 不要设置id，因为使用了自增主键
        // 保存到数据库:
        sessionFactory.getCurrentSession().persist(user);
        // 现在已经自动获得了id:
        System.out.println(user.getId());
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteUser(Long id) {
        User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
        if (user != null) {
            sessionFactory.getCurrentSession().remove(user);
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(Long id, String name) {
        User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
        user.setName(name);
        sessionFactory.getCurrentSession().merge(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User login(String email, String password) {
        List<User> list = sessionFactory.getCurrentSession()
                .createQuery("from User u where u.email = ?1 and u.password = ?2", User.class)
                .setParameter(1, email)
                .setParameter(2, password)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<User> getUsers(int pageIndex) {
        int pageSize = 100;
        return sessionFactory.getCurrentSession().createQuery("from User u", User.class).setFirstResult((pageIndex - 1) * pageSize).setMaxResults(pageSize)
                .list();
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public List<User> fetchUsers() {
        List<User> list = sessionFactory.getCurrentSession()
                .createQuery("* from User u ", User.class)
                .list();
        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User fetchUserById(long id) {
//        sessionFactory.getCurrentSession().byId(User.class).load(id);
        List<User> list = sessionFactory.getCurrentSession()
                .createQuery("from User u where u.id = ?1", User.class)
                .setParameter(1, id)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public User fetchUserByEmail(String email) {
        List<User> list = sessionFactory.getCurrentSession()
                .createQuery("from User u where u.email = ?1", User.class)
                .setParameter(1, email)
                .list();
        return list.isEmpty() ? null : list.get(0);
    }

}
