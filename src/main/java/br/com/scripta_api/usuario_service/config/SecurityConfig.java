package br.com.scripta_api.usuario_service.config;

// Se não estiver usando os imports do JWT, pode remover, mas mantive para não quebrar compilação
import br.com.scripta_api.usuario_service.security.JwtAuthenticatedFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Mantive as variáveis declaradas para o Lombok não reclamar,
    // mas não vamos usá-las na configuração abaixo.
    private final JwtAuthenticatedFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Habilita CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Desabilita CSRF
                .csrf(csrf -> csrf.disable())

                // Sessão Stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // --- LIBERA GERAL (MODO APRESENTAÇÃO) ---
                .authorizeHttpRequests(auth -> auth
                        // Aceita tudo sem perguntar quem é
                        .anyRequest().permitAll()
                )

                // H2 Console
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        // --- CRUCIAL: COMENTEI OS FILTROS ABAIXO ---
        // Se deixasse ativo, o Java tentaria validar o token fake e daria erro.
        // .authenticationProvider(authenticationProvider)
        // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite qualquer origem (Front local ou Vercel)
        configuration.setAllowedOrigins(List.of("*"));

        // Permite todos os métodos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));

        // Permite todos os headers
        configuration.setAllowedHeaders(List.of("*"));

        // Expõe o header de autorização (caso precisasse)
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}