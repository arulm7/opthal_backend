package com.example.opthal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Permit preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // =========================
                        // REGISTER + LOGIN
                        // =========================

                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password",
                                "/error"
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}