package br.com.scripta_api.usuario_service.dto;

import br.com.scripta_api.usuario_service.application.domain.TipoDeConta;
import br.com.scripta_api.usuario_service.infra.data.UsuarioEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String matricula;
    private TipoDeConta tipoDeConta;

    // Método auxiliar (opcional, mas ajuda a limpar o controller)
    public static UsuarioResponse fromEntity(UsuarioEntity entity) {
        return new UsuarioResponse(
                entity.getId(),
                entity.getNome(),
                entity.getMatricula(),
                entity.getTipoDeConta()
        );
    }
}