package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.User;
import com.cenkc.digitalwallet.entity.dto.UserRequestDTO;
import com.cenkc.digitalwallet.entity.dto.UserResponseDTO;
import com.cenkc.digitalwallet.exception.ResourceNotFoundException;
import com.cenkc.digitalwallet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        // Set up SecurityContext mock
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Create a test user
        testUser = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .tckn("12345678901")
                .username("testuser")
                .email("test@example.com")
                .password("encoded_password")
                .roles(new HashSet<>())
                .build();

        // Create a user request DTO
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFirstName("New");
        userRequestDTO.setLastName("User");
        userRequestDTO.setTckn("98765432109");
        userRequestDTO.setUsername("newuser");
        userRequestDTO.setEmail("new@example.com");
        userRequestDTO.setPassword("password123");
    }

    @Nested
    @DisplayName("getCurrentUser() Tests")
    class GetCurrentUserTests {

        @Test
        @DisplayName("Should return the current user when found")
        void getCurrentUser_ShouldReturnCurrentUser_WhenUserExists() {
            // Arrange
            when(authentication.getName()).thenReturn("testuser");
            when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

            // Act
            User result = userService.getCurrentUser();

            // Assert
            assertNotNull(result);
            assertEquals(testUser.getId(), result.getId());
            assertEquals(testUser.getUsername(), result.getUsername());
            assertEquals(testUser.getEmail(), result.getEmail());

            // Verify
            verify(userRepository).findByUsername("testuser");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user not found")
        void getCurrentUser_ShouldThrowException_WhenUserNotFound() {
            // Arrange
            when(authentication.getName()).thenReturn("nonexistentuser");
            when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> userService.getCurrentUser());

            // Verify
            verify(userRepository).findByUsername("nonexistentuser");
        }
    }

    @Nested
    @DisplayName("addUser() Tests")
    class AddUserTests {

        @Test
        @DisplayName("Should successfully add a new user")
        void addUser_ShouldAddUser_WhenValidRequest() {
            // Arrange
            User currentUser = testUser;
            User savedUser = User.builder()
                    .id(2L)
                    .firstName(userRequestDTO.getFirstName())
                    .lastName(userRequestDTO.getLastName())
                    .tckn(userRequestDTO.getTckn())
                    .username(userRequestDTO.getUsername())
                    .email(userRequestDTO.getEmail())
                    .password("encoded_password")
                    .build();

            when(authentication.getName()).thenReturn("testuser");
            when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(currentUser));
            when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("encoded_password");
            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            // Act
            UserResponseDTO result = userService.addUser(userRequestDTO);

            // Assert
            assertNotNull(result);
            assertEquals(userRequestDTO.getFirstName(), result.getFirstName());
            assertEquals(userRequestDTO.getLastName(), result.getLastName());
            assertEquals(userRequestDTO.getUsername(), result.getUsername());
            assertEquals(userRequestDTO.getEmail(), result.getEmail());

            // Verify the User object created
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            User capturedUser = userCaptor.getValue();

            assertEquals(userRequestDTO.getFirstName(), capturedUser.getFirstName());
            assertEquals(userRequestDTO.getLastName(), capturedUser.getLastName());
            assertEquals(userRequestDTO.getTckn(), capturedUser.getTckn());
            assertEquals(userRequestDTO.getUsername(), capturedUser.getUsername());
            assertEquals(userRequestDTO.getEmail(), capturedUser.getEmail());
            assertEquals("encoded_password", capturedUser.getPassword());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when current user not found")
        void addUser_ShouldThrowException_WhenCurrentUserNotFound() {
            // Arrange
            when(authentication.getName()).thenReturn("nonexistentuser");
            when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> userService.addUser(userRequestDTO));

            // Verify
            verify(userRepository).findByUsername("nonexistentuser");
            verify(userRepository, never()).save(any(User.class));
        }
    }
}