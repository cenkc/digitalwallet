package com.cenkc.digitalwallet.auth;

import com.cenkc.digitalwallet.util.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

/*
    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> foundUser = userRepository.findByUsername(username);
            if (foundUser.isEmpty()) {
                throw new UsernameNotFoundException("User not found");
            }
            User user = foundUser.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toSet())
            );
        };
    }
*/

    @Bean(name = "pwdEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
                // Disable frame options to allow H2 console to render correctly
                // The H2 console is rendered inside an iframe.
                // By default, Spring Security’s frame options are set to deny this,
                // so we need to disable them.
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .authorizeHttpRequests(authorize -> {
                authorize.requestMatchers("/h2-console/**").permitAll();
                authorize.requestMatchers("/api/v1/**").authenticated();
                authorize.anyRequest().permitAll();
            })
            // Spring Security uses its own exception handling chain.
            // When an authorization exception occurs,
            // it’s handled by the AccessDeniedHandler
            // rather than being propagated to our controller advice (GlobalExceptionHandler class).
            .exceptionHandling(exceptionHandling ->
                    exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            String responseJson = String.format(
                                    AppConstants.STANDART_EXCEPTION_RESPONSE_JSON,
                                    LocalDateTime.now(),
                                    "Access Denied",
                                    request.getRequestURI(),
                                    request.getMethod());
                            response.getWriter().write(responseJson);

                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            String responseJson = String.format(
                                    AppConstants.STANDART_EXCEPTION_RESPONSE_JSON,
                                    LocalDateTime.now(),
                                    "Unauthorized",
                                    request.getRequestURI(),
                                    request.getMethod());
                            response.getWriter().write(responseJson);
                        })
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
