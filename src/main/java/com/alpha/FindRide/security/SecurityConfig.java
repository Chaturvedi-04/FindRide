package com.alpha.FindRide.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ✅ Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())

            // ✅ Disable default auth mechanisms
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())

            // ✅ Authorization rules
            .authorizeHttpRequests(auth -> auth

                // 🔓 PUBLIC REGISTRATION (POST ONLY)
                .requestMatchers(HttpMethod.POST, "/customer/saveCustomer").permitAll()
                .requestMatchers(HttpMethod.POST, "/driver/saveDriver").permitAll()

                // 🔐 AUTH REQUIRED
                .requestMatchers("/customer/auth/**", "/driver/auth/**").authenticated()

                // Everything else public for now
                .anyRequest().permitAll()
            );

        return http.build();
    }
}