package br.com.scripta_api.usuario_service.repository;

import br.com.scripta_api.usuario_service.infra.data.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByMatricula(String matricula);
}