package br.com.scripta_api.usuario_service.dto;

import br.com.scripta_api.usuario_service.application.domain.TipoDeConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriarUsuarioRequest {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "A matrícula é obrigatória")
    private String matricula;

    private String senha;

    @NotNull(message = "O tipo de conta é obrigatório")
    private TipoDeConta tipoDeConta;


    private String status;
}