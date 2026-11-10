package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.account.EmailAlreadyExistsException;
import co.ufps.trainingcenter.account.User;
import co.ufps.trainingcenter.account.UserId;
import co.ufps.trainingcenter.account.UserProfile;
import co.ufps.trainingcenter.application.port.in.RegisterUserCommand;
import co.ufps.trainingcenter.application.port.out.PasswordHasher;
import co.ufps.trainingcenter.application.port.out.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private RegisterUserService registerUserService;

    @Test
    void shouldRegisterNewUserSuccessfully() {
        RegisterUserCommand command = new RegisterUserCommand(
                "test@example.com", "testuser", "password123"
        );

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordHasher.hash("password123")).thenReturn("$2a$12$hashed");

        UserId result = registerUserService.register(command);

        assertNotNull(result);
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordHasher).hash("password123");
        verify(userRepository).save(any(User.class), any(UserProfile.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterUserCommand command = new RegisterUserCommand(
                "existing@example.com", "newuser", "password123"
        );

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () ->
                registerUserService.register(command)
        );

        verify(userRepository, never()).save(any(User.class), any(UserProfile.class));
    }

    @Test
    void shouldThrowExceptionForNullEmail() {
        RegisterUserCommand command = new RegisterUserCommand(
                null, "testuser", "password123"
        );

        assertThrows(Exception.class, () ->
                registerUserService.register(command)
        );
    }

    @Test
    void shouldThrowExceptionForBlankUsername() {
        RegisterUserCommand command = new RegisterUserCommand(
                "test@example.com", "  ", "password123"
        );

        assertThrows(Exception.class, () ->
                registerUserService.register(command)
        );
    }

    @Test
    void shouldThrowExceptionForBlankPassword() {
        RegisterUserCommand command = new RegisterUserCommand(
                "test@example.com", "testuser", ""
        );

        assertThrows(Exception.class, () ->
                registerUserService.register(command)
        );
    }
}
