package br.com.scripta_api.usuario_service.repository;

import br.com.scripta_api.usuario_service.infra.data.LivroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<LivroEntity, Long> {

    Optional<LivroEntity> findByIsbn(String isbn);

    @Query("SELECT l FROM LivroEntity l WHERE LOWER(l.titulo) LIKE %:termo% OR LOWER(l.autor) LIKE %:termo%")
    List<LivroEntity> buscarPorTituloOuAutor(String termo);
}
