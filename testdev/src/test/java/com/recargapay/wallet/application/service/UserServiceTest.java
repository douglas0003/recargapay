package com.recargapay.wallet.application.service;

import com.recargapay.wallet.domain.model.User;
import com.recargapay.wallet.domain.ports.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUserAvoidingNullPointer() {
        String userName = "John Doe";
        UUID userId = UUID.randomUUID();

        User mockUser = new User(userName);
        mockUser.setId(userId);

        // Mocking the repository: ensures save() always returns a valid user
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User createdUser = assertDoesNotThrow(() -> userService.createUser(userName),
                "User creation should not throw NullPointerException");

        assertNotNull(createdUser, "Created user should not be null");
        assertNotNull(createdUser.getId(), "User ID should not be null");
        assertEquals(userId, createdUser.getId(), "User ID should match the expected value");
        assertEquals(userName, createdUser.getName(), "User name should match the expected value");

        verify(userRepository, times(1)).save(any(User.class));
    }
}
