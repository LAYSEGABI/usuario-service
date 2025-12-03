package br.com.scripta_api.usuario_service.dto;

import lombok.Data;

@Data
public class AtualizarLivroRequest {

    private String titulo;
    private String autor;
    private String isbn;
    private Integer anoPublicacao;
    private Integer quantidadeTotal;
    private Integer quantidadeDisponivel;

}
