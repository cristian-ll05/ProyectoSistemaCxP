package com.proyecto.cuentasporpagar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF temporalmente para facilitar las pruebas
            .authorizeHttpRequests(auth -> auth
                // ESTO ES LO MÁS IMPORTANTE:
                // Permite acceso total a login, registro y archivos estáticos
                .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/images/**").permitAll()
                // Cualquier otra ruta requiere estar logueado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .loginProcessingUrl("/login")
                // CAMBIA ESTA LÍNEA:
                .defaultSuccessUrl("/dashboard", true) // <--- Ahora irá al index principal
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );
            
        return http.build();
    }
}