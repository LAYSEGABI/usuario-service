package br.com.scripta_api.usuario_service.application.gateways.service;

import br.com.scripta_api.usuario_service.infra.data.LivroEntity;

public interface GoogleBooksService {
    LivroEntity buscarLivroPorIsbn(String isbn);
}
