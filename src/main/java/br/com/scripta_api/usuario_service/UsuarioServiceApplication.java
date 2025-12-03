package br.com.scripta_api.usuario_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// AQUI ESTÁ O TRUQUE: Manda o Spring olhar a pasta "pai" para achar TANTO Usuario QUANTO Catalogo
@ComponentScan(basePackages = "br.com.scripta_api")
@EnableJpaRepositories(basePackages = "br.com.scripta_api")
@EntityScan(basePackages = "br.com.scripta_api")
public class UsuarioServiceApplication {

    public static void main(String[] args) {
        // CORRIGIDO: Agora usa '.class' em vez de '.java'
        SpringApplication.run(UsuarioServiceApplication.class, args);
    }

}