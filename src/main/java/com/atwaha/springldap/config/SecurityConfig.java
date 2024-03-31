package com.atwaha.springldap.config;

import com.atwaha.springldap.model.Token;
import com.atwaha.springldap.repository.TokenRepository;
import com.atwaha.springldap.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   TokenRepository tokenRepository) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.authorizeHttpRequests(
                requests -> {
                    requests
                            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/login", "/h2-console/**")
                            .permitAll();
                    requests
                            .anyRequest()
                            .fullyAuthenticated();
                }
        );
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exception -> {
            exception.accessDeniedHandler((request, response, accessDeniedException) -> {
                /* You have a token, but it doesn't have appropriate authorities to access the requested resources */
                response.setStatus(FORBIDDEN.value());
            });
            exception.authenticationEntryPoint((request, response, authException) -> {
                /* No Token or Token is Invalid */
                response.setStatus(UNAUTHORIZED.value());
            });
        });
        http.logout(logout -> {
            logout.logoutUrl("/api/v1/auth/logout");
            logout.logoutSuccessHandler((request, response, authentication) -> {
                response.setStatus(HttpStatus.OK.value());
            });
            logout.addLogoutHandler((request, response, authentication) -> {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return;
                }

                String token = authHeader.substring(7);
                Token storedToken = tokenRepository.findByToken(token);

                if (storedToken != null) {
                    storedToken.setRevoked(true);
                    tokenRepository.save(storedToken);
                }
            });
        });
        return http.build();
    }

    @Bean
    ActiveDirectoryLdapAuthenticationProvider authenticationProvider() {
        return new ActiveDirectoryLdapAuthenticationProvider("net.orci", "ldap://192.168.1.12:389");
    }
}
