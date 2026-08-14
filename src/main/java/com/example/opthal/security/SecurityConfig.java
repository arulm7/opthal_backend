package com.example.opthal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // REGISTER + LOGIN
                        // =========================

                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()


                        // =========================
                        // ANSWERS
                        // =========================

                        // USER + ADMIN can view answers
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/questions/*/answers"
                        ).hasAnyRole("USER", "ADMIN")

                        // ADMIN can create TEXT/TABLE answers
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/questions/*/answers/**"
                        ).hasRole("ADMIN")


                        // =========================
                        // QUESTIONS
                        // =========================

                        // USER + ADMIN can view questions
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/questions/**"
                        ).hasAnyRole("USER", "ADMIN")

                        // ADMIN can create questions
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/questions"
                        ).hasRole("ADMIN")

                        // ADMIN can update questions
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/questions/**"
                        ).hasRole("ADMIN")

                        // ADMIN can delete questions
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/questions/**"
                        ).hasRole("ADMIN")


                        // =========================
                        // EVERYTHING ELSE
                        // =========================

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}