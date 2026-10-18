package com.digitalfix.bff.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Seguridad del BFF
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Deshabilita CSRF porque utilizamos JWT Bearer
            .csrf(csrf -> csrf.disable())

            // Habilita CORS con la configuración definida abajo
            .cors(Customizer.withDefaults())

            .authorizeHttpRequests(auth -> auth

                // Permite solicitudes preflight sin autenticación
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Protege todos los endpoints de la API
                .requestMatchers("/api/**").authenticated()

                // Las demás solicitudes también requieren autenticación
                .anyRequest().authenticated()
            )

            // Autenticación mediante JWT de Azure AD
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(Customizer.withDefaults())
            );

        return http.build();
    }

    // Configuración CORS para permitir peticiones desde React
    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${digitalfix.cors.allowed-origin:http://localhost:5173}")
            String allowedOrigin) {

        CorsConfiguration config = new CorsConfiguration();

        // Origen permitido: frontend React
        config.setAllowedOrigins(List.of(allowedOrigin));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of(
            "GET",
            "POST",
            "PUT",
            "PATCH",
            "DELETE",
            "OPTIONS"
        ));

        // Encabezados permitidos
        config.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "Accept"
        ));

        // Tiempo de caché de solicitudes preflight
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        // Aplica CORS a todos los endpoints
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // Validación del JWT de Azure AD
    @Bean
    JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
            String issuer,

            @Value("${security.jwt.audience}")
            String audience) {

        NimbusJwtDecoder decoder =
                (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuer);

        // Valida issuer, firma, expiración y tiempos del token
        OAuth2TokenValidator<Jwt> issuerAndTimestamp =
                JwtValidators.createDefaultWithIssuer(issuer);

        // Verifica que el token esté destinado a nuestra API
        OAuth2TokenValidator<Jwt> audienceValidator = jwt -> {

            List<String> audiences = jwt.getAudience();

            if (audiences != null && audiences.contains(audience)) {
                return OAuth2TokenValidatorResult.success();
            }

            OAuth2Error error = new OAuth2Error(
                    "invalid_token",
                    "The required audience is missing",
                    null
            );

            return OAuth2TokenValidatorResult.failure(error);
        };

        // Combina ambas validaciones
        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        issuerAndTimestamp,
                        audienceValidator
                )
        );

        return decoder;
    }
}