package ru.solarev.taskmanagementapi.service;

import ru.solarev.taskmanagementapi.dto.UserDto;
import ru.solarev.taskmanagementapi.entity.user.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User findByEmail(String email);
    User findByName(String name);
    User create(User user);
    User update(User updateUser, String email);
    void delete(Long id, String email);
    User addUserAdminRole(Long id);
}
