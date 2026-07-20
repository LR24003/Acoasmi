package com.acoasmi.roble.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(

                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",

                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",

                                "/api/auth/**",

                                "/api/usuarios/**",
                                "/api/permisos/**",
                                "/api/roles/**",
                                "/api/catalogo-cuentas/**",
                                "/api/asociados/**",
                                "/api/departamentos/**",
                                "/api/municipios/**",
                                "/api/distritos/**",
                                "/api/actividades-economicas/**",
                                "/api/cumplimiento-riesgos/**",
                                "/api/asociados-documentos/**",
                                "/api/control-cajas/**",
                                "/api/beneficiarios/**",
                                "/api/asociado-cuentas/**"

                        ).permitAll()

                        .anyRequest().authenticated()
                );

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationEventPublisher authenticationEventPublisher(
            org.springframework.context.ApplicationEventPublisher applicationEventPublisher) {
        return new org.springframework.security.authentication.DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }
}