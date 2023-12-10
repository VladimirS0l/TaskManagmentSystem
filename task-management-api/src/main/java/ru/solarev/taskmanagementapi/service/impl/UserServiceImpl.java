package ru.solarev.taskmanagementapi.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.solarev.taskmanagementapi.dto.UserDto;
import ru.solarev.taskmanagementapi.dto.mapper.UserMapper;
import ru.solarev.taskmanagementapi.entity.user.Role;
import ru.solarev.taskmanagementapi.entity.user.User;
import ru.solarev.taskmanagementapi.exceptions.AccessDeniedException;
import ru.solarev.taskmanagementapi.exceptions.ResourceNotFoundException;
import ru.solarev.taskmanagementapi.repository.UserRepository;
import ru.solarev.taskmanagementapi.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пользователь не найден"));
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пользователь не найден"));
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пользователь не найден"));
    }

    @Override
    public User create(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Пользователь с таким email уже существует");
        }
        if (userRepository.findByName(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Пользователь с таким именем уже существует");
        }
        if (!user.getPassword().equals(user.getConfirmedPassword())) {
            throw new IllegalStateException(
                    "Пароли не совпадают"
            );
        }
        user.setId(null);
        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User update(User updateUser, String email) {
        if (!updateUser.getPassword().equals(updateUser.getConfirmedPassword())) {
            throw new IllegalStateException(
                    "Пароли не совпадают"
            );
        }
        var user = userRepository
                .findById(updateUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пользователь не найден"));
        user.setName(updateUser.getName());
        user.setPassword(updateUser.getPassword());
        user.setEmail(updateUser.getEmail());
        user.setUpdatedDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id, String email) {
        var user = userRepository.findById(id);
        if (user.get().getEmail().equals(email)) {
            user.ifPresent(userRepository::delete);
        }
    }

    @Override
    public User addUserAdminRole(Long id) {
        var user = findById(id);
        user.setRoles(Collections.singleton(new Role(2, "ROLE_ADMIN")));
        return userRepository.save(user);
    }
}
