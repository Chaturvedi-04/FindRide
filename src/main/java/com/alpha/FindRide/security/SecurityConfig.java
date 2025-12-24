package com.alpha.FindRide.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 🔐 Password encoder for storing passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔐 Security rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())

            // Authorization rules
            .authorizeHttpRequests(auth -> auth

                // 🔓 Public endpoints (NO authentication)
                .requestMatchers(
                    "/",
                    "/health",
                    "/customer/saveCustomer",
                    "/driver/saveDriver"
                ).permitAll()

                // 🔐 Secure only /auth/** endpoints
                .requestMatchers("/customer/auth/**","/driver/auth/**").authenticated()

                // Everything else is allowed (you can change to authenticated if needed)
                .anyRequest().permitAll()
            );

        return http.build();
    }
}