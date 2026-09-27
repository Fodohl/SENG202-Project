package seng202.team1.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityChecker {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, TokenChecker tokenChecker) throws Exception {
        return http
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/heartbeat", "/api/v1/auth/**", "/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(tokenChecker, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

}
