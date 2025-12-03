package br.com.scripta_api.usuario_service.controller;

import br.com.scripta_api.usuario_service.dto.AtualizarLivroRequest;
import br.com.scripta_api.usuario_service.dto.CriarLivroRequest;
import br.com.scripta_api.usuario_service.dto.LivroResponse;
import br.com.scripta_api.usuario_service.infra.data.LivroEntity;
import br.com.scripta_api.usuario_service.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.scripta_api.usuario_service.application.gateways.service.GoogleBooksService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/livros") // Rota correta para Livros
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LivroController { // Nome correto da classe

    private final LivroRepository livroRepository;
    private final GoogleBooksService googleBooksService;

    @PostMapping("/importar/{isbn}")
    public ResponseEntity<LivroResponse> importarLivro(@PathVariable String isbn) {
        LivroEntity livro = googleBooksService.buscarLivroPorIsbn(isbn);
        LivroEntity salvo = livroRepository.save(livro);
        return ResponseEntity.ok(LivroResponse.fromEntity(salvo));
    }

    // --- CRIAR ---
    @PostMapping
    public ResponseEntity<LivroResponse> criarLivro(@RequestBody CriarLivroRequest request) {
        LivroEntity entity = new LivroEntity();
        entity.setTitulo(request.getTitulo());
        entity.setAutor(request.getAutor());
        entity.setIsbn(request.getIsbn());
        entity.setAnoPublicacao(request.getAnoPublicacao());

        // Garante valores padrão (evita erro 500 se vier nulo)
        entity.setQuantidadeTotal(request.getQuantidadeTotal() != null ? request.getQuantidadeTotal() : 1);
        entity.setQuantidadeDisponivel(request.getQuantidadeDisponivel() != null ? request.getQuantidadeDisponivel() : 1);

        LivroEntity salvo = livroRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(LivroResponse.fromEntity(salvo));
    }

    // --- LISTAR ---
    @GetMapping
    public ResponseEntity<List<LivroResponse>> listarLivros() {
        List<LivroResponse> response = livroRepository.findAll().stream()
                .map(LivroResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // --- BUSCAR POR ID ---
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponse> buscarPorId(@PathVariable Long id) {
        LivroEntity livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        return ResponseEntity.ok(LivroResponse.fromEntity(livro));
    }

    // --- BUSCA SIMPLES (POST para facilitar envio de JSON ou String) ---
    @PostMapping("/buscar")
    public ResponseEntity<List<LivroResponse>> buscarLivrosPorPalavra(@RequestBody String palavra) {
        String termo = palavra.replace("\"", ""); // Limpa aspas se vierem do front
        List<LivroResponse> response = livroRepository.buscarPorTituloOuAutor(termo.toLowerCase()).stream()
                .map(LivroResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    // --- ATUALIZAR ---
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> atualizarLivro(@PathVariable Long id, @RequestBody AtualizarLivroRequest request) {
        LivroEntity entity = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (request.getTitulo() != null) entity.setTitulo(request.getTitulo());
        if (request.getAutor() != null) entity.setAutor(request.getAutor());
        if (request.getIsbn() != null) entity.setIsbn(request.getIsbn());
        if (request.getAnoPublicacao() != null) entity.setAnoPublicacao(request.getAnoPublicacao());

        // Atualiza estoque se vier no request
        if (request.getQuantidadeTotal() != null) entity.setQuantidadeTotal(request.getQuantidadeTotal());
        if (request.getQuantidadeDisponivel() != null) entity.setQuantidadeDisponivel(request.getQuantidadeDisponivel());

        LivroEntity atualizado = livroRepository.save(entity);
        return ResponseEntity.ok(LivroResponse.fromEntity(atualizado));
    }

    // --- INCREMENTAR ESTOQUE ---
    @PutMapping("/{id}/estoque/incrementar")
    public ResponseEntity<LivroResponse> incrementarEstoque(@PathVariable Long id) {
        LivroEntity entity = livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrado"));
        entity.setQuantidadeDisponivel(entity.getQuantidadeDisponivel() + 1);
        entity.setQuantidadeTotal(entity.getQuantidadeTotal() + 1);
        return ResponseEntity.ok(LivroResponse.fromEntity(livroRepository.save(entity)));
    }

    // --- DECREMENTAR ESTOQUE ---
    @PutMapping("/{id}/estoque/decrementar")
    public ResponseEntity<LivroResponse> decrementarEstoque(@PathVariable Long id) {
        LivroEntity entity = livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrado"));
        if (entity.getQuantidadeDisponivel() > 0) {
            entity.setQuantidadeDisponivel(entity.getQuantidadeDisponivel() - 1);
            entity.setQuantidadeTotal(entity.getQuantidadeTotal() - 1);
        }
        return ResponseEntity.ok(LivroResponse.fromEntity(livroRepository.save(entity)));
    }

    // --- IMPORTAR (Dummy) ---
    @PostMapping("/importar/{isbn}")
    public ResponseEntity<LivroResponse> importaLivro(@PathVariable String isbn) {
        LivroEntity dummy = new LivroEntity();
        dummy.setTitulo("Livro Importado (" + isbn + ")");
        dummy.setAutor("Autor Desconhecido");
        dummy.setIsbn(isbn);
        dummy.setAnoPublicacao(2023);
        dummy.setQuantidadeTotal(1);
        dummy.setQuantidadeDisponivel(1);
        LivroEntity salvo = livroRepository.save(dummy);
        return ResponseEntity.ok(LivroResponse.fromEntity(salvo));
    }

    // --- DELETAR ---
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        livroRepository.deleteById(id);
    }
}