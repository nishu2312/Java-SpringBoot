package com.taskmanager.service;

import java.util.List;

import com.taskmanager.model.User;

public interface IUserService {
    User createUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
}
