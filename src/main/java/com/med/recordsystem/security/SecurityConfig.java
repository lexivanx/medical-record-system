package com.med.recordsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import jakarta.servlet.http.Cookie;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/access-denied");
                        })
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/doctor/**").hasRole("DOCTOR")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/reports/**").hasRole("ADMIN")
                        .requestMatchers("/patients/visits", "/sick-leaves").hasRole("PATIENT")
                        .requestMatchers( "/","/register", "/login", "/error", "/api/users/register", "api/users/doctors",
                                "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/patients/visits", "/api/visits/me").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandler())
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }

    private LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            Cookie cookie = new Cookie("loggedInUsername", null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            cookie.setSecure(true); // Enable for HTTPS
            cookie.setAttribute("SameSite", "Strict");
            response.addCookie(cookie);
        };
    }
}
