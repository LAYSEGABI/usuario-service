package br.com.scripta_api.usuario_service.config;

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

    private final JwtAuthenticatedFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Habilita o CORS usando a configuração do @Bean abaixo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Desabilita CSRF (não necessário para APIs REST Stateless)
                .csrf(csrf -> csrf.disable())

                // Define a política de sessão como STATELESS (não guarda sessão no servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura as permissões de acesso
                .authorizeHttpRequests(auth -> auth
                                // Libera endpoints públicos (Login, Swagger, H2 Console, Raiz)
                                .requestMatchers("/auth/**", "/h2-console/**", "/", "/error").permitAll()
                                // Libera OPTIONS para o pré-voo do CORS (importante para o navegador)
                                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                                // PARA TESTES: Libera tudo (remover em produção)
                                .anyRequest().permitAll()
                        // EM PRODUÇÃO: Comente a linha acima e descomente a de baixo
                        // .anyRequest().authenticated()
                )

                // Configuração para o H2 Console funcionar (se estiver usando)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Adiciona o provedor de autenticação e o filtro JWT
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));

        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(List.of("Authorization"));

        // Aplica para todas as rotas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}