package br.com.scripta_api.usuario_service.dto;

import br.com.scripta_api.usuario_service.infra.data.LivroEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivroResponse {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer quantidadeDisponivel;

    public static LivroResponse fromEntity(LivroEntity entity) {
        return new LivroResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getAutor(),
                entity.getIsbn(),
                entity.getQuantidadeDisponivel()
        );
    }
}