package com.fintech.Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://localhost:3001", 
            "http://127.0.0.1:3000",
            "http://127.0.0.1:3001"
        ));
        
        // Allow all HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        
        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // Expose headers
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/**", "/users/**", "/admin/**", "/account/**", "/h2-console/**", "/error").permitAll()
                .anyRequest().permitAll() // Allow all requests for now
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Updated method for H2 console
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(
                org.springframework.security.config.http.SessionCreationPolicy.STATELESS
            ));

        return http.build();
    }
}