package com.recargapay.wallet.application.service;

import com.recargapay.wallet.domain.model.User;
import com.recargapay.wallet.domain.ports.UserRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepositoryPort userRepository;

    public UserService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }

        User user = new User(name);
        user = userRepository.save(user);

        logger.info("New user created: ID = {}, Name = {}", user.getId(), user.getName());
        return user;
    }
}