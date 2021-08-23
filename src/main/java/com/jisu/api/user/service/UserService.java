package com.jisu.api.user.service;

import com.jisu.api.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    void save(User user);

    boolean existsById(Long id);

    Long count();

    void deleteById(Long id);
}
