package br.com.scripta_api.usuario_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // Importante

// Removemos o ManagementWeb... e deixamos só o SecurityAutoConfiguration
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = "br.com.scripta_api")
@EnableJpaRepositories(basePackages = "br.com.scripta_api")
@EntityScan(basePackages = "br.com.scripta_api")
public class UsuarioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsuarioServiceApplication.class, args);
    }

}