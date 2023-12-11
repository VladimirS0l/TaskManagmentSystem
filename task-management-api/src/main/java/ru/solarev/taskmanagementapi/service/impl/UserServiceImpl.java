package ru.solarev.taskmanagementapi.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solarev.taskmanagementapi.entity.user.Role;
import ru.solarev.taskmanagementapi.entity.user.User;
import ru.solarev.taskmanagementapi.exceptions.ResourceAccessDeniedException;
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
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::findById", key="#id")
    public User findById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пользователь не найден"));
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::findByEmail", key="#email")
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пользователь не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::findByName", key="#name")
    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пользователь не найден"));
    }

    @Override
    @Transactional
//    @Cacheable(value = "UserService::findById", key="#user.id", condition = "#user.id!=null")
    @Caching(cacheable = {
            @Cacheable(value = "UserService::findById", key="#user.id", condition = "#user.id!=null"),
            @Cacheable(value = "UserService::findByName", key="#user.name", condition = "#user.name!=null"),
            @Cacheable(value = "UserService::findByEmail", key="#user.email", condition = "#user.email!=null")
    })
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
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::findById", key="#updateUser.id", condition = "#updateUser.id!=null"),
            @CachePut(value = "UserService::findByName", key="#updateUser.name", condition = "#updateUser.name!=null"),
            @CachePut(value = "UserService::findByEmail", key="#updateUser.email", condition = "#updateUser.email!=null")
    })
    public User update(User updateUser, String email) {
        if (!updateUser.getPassword().equals(updateUser.getConfirmedPassword())) {
            throw new IllegalStateException(
                    "Пароли не совпадают"
            );
        }
        var user = findById(updateUser.getId());
        user.setName(updateUser.getName());
        user.setPassword(updateUser.getPassword());
        user.setEmail(updateUser.getEmail());
        user.setUpdatedDate(LocalDateTime.now());
        if (!user.getEmail().equals(email)) {
            throw new ResourceAccessDeniedException("Вы не можете изменить другого пользователя");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::findById", key="#id")
    public void delete(Long id, String email) {
        var user = findById(id);
        if (user.getEmail().equals(email)) {
            userRepository.delete(user);
        } else {
            throw new ResourceAccessDeniedException("Вы не можете удалить другого пользователя");
        }
    }

    @Override
    @Transactional
    @CachePut(value = "UserService::findById", key="#id")
    public User addUserAdminRole(Long id) {
        var user = findById(id);
        user.setRoles(Collections.singleton(new Role(2, "ROLE_ADMIN")));
        return userRepository.save(user);
    }
}
