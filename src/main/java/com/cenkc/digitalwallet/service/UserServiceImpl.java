package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.User;
import com.cenkc.digitalwallet.entity.dto.UserRequestDTO;
import com.cenkc.digitalwallet.entity.dto.UserResponseDTO;
import com.cenkc.digitalwallet.exception.ResourceNotFoundException;
import com.cenkc.digitalwallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           @Qualifier("pwdEncoder") PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get the currently authenticated user
     *
     * @return
     */
    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * @param userRequestDTO
     * @return
     */
    @Override
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {
        // Check if the user already exists
        User currentUser = getCurrentUser();

        // convert UserRequestDTO to User entity
        User newUser = User.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .tckn(userRequestDTO.getTckn())
                .username(userRequestDTO.getUsername())
                .email(userRequestDTO.getEmail())
                // normally frontend should encode the password and send
                //.password(currentUser.getPassword())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .build();
        // persist the user
        User savedUser = userRepository.save(newUser);
        // Convert to DTO and return
        return UserResponseDTO.builder()
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
    }

}
