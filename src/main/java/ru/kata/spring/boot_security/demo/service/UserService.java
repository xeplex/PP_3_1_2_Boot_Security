package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    void save(User user);

    User getById(Long id);

    void delete(Long id);

    void update(User user, Long id);

    User findByUsername(String username);

    User findByEmail(String email);
}
