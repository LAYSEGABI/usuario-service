package br.com.scripta_api.usuario_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean; // Importante
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.cors.CorsConfiguration; // Importante
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Importante
import org.springframework.web.filter.CorsFilter; // Importante

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.scripta_api")
@EnableJpaRepositories(basePackages = "br.com.scripta_api")
@EntityScan(basePackages = "br.com.scripta_api")
public class UsuarioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsuarioServiceApplication.class, args);
    }

    // --- A MARRETADA DO CORS ---

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();


        config.setAllowCredentials(true);


        config.setAllowedOriginPatterns(Collections.singletonList("*"));


        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));


        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));


        config.setExposedHeaders(Arrays.asList("Authorization", "Link", "X-Total-Count"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}