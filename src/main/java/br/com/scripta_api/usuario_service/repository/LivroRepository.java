package br.com.scripta_api.usuario_service.repository;

import br.com.scripta_api.usuario_service.infra.data.LivroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<LivroEntity, Long> {

    // Esta busca personalizada permite procurar por Título OU Autor na mesma barra de pesquisa
    @Query("SELECT l FROM LivroEntity l WHERE LOWER(l.titulo) LIKE %:termo% OR LOWER(l.autor) LIKE %:termo%")
    List<LivroEntity> buscarPorTituloOuAutor(@Param("termo") String termo);
}