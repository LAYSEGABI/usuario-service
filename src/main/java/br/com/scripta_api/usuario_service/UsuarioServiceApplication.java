package br.com.scripta_api.usuario_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // IMPORTANTE
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // IMPORTANTE

@SpringBootApplication
// 1. Procura Controllers e Services em todo o projeto
@ComponentScan(basePackages = "br.com.scripta_api")
// 2. Procura as Interfaces de Banco de Dados (Repositories) em todo o projeto
@EnableJpaRepositories(basePackages = "br.com.scripta_api")
// 3. Procura as Tabelas (Entities) em todo o projeto
@EntityScan(basePackages = "br.com.scripta_api")
public class UsuarioServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsuarioServiceApplication.class, args);
    }

}