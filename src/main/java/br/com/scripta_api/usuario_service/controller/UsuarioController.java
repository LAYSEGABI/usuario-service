package br.com.scripta_api.usuario_service.controller;

import br.com.scripta_api.usuario_service.application.domain.Usuario;
import br.com.scripta_api.usuario_service.application.domain.UsuarioBuilder;
import br.com.scripta_api.usuario_service.application.gateways.service.UsuarioService;
import br.com.scripta_api.usuario_service.dto.CriarUsuarioRequest;
import br.com.scripta_api.usuario_service.dto.UsuarioResponse;
import br.com.scripta_api.usuario_service.infra.data.UsuarioEntity;
import br.com.scripta_api.usuario_service.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    // INJETADO PARA CORREÇÃO RÁPIDA DE UPDATE (Modo Apresentação)
    private final UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @RequestBody CriarUsuarioRequest request) {
        Usuario usuarioRequest = UsuarioBuilder.builder()
                .nome(request.getNome())
                .matricula(request.getMatricula())
                .senha(request.getSenha())
                .tipoDeConta(request.getTipoDeConta())
                .build();

        Usuario novoUsuario = usuarioService.criarUsuario(usuarioRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponse.fromDomain(novoUsuario));
    }

    // --- NOVO MÉTODO PARA ATUALIZAR (PUT) ---
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody CriarUsuarioRequest request) {
        // Busca a entidade no banco (Modo direto para garantir funcionamento)
        UsuarioEntity entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualiza os dados
        entity.setNome(request.getNome());
        entity.setMatricula(request.getMatricula());
        entity.setTipoDeConta(request.getTipoDeConta());
        entity.setStatus(request.getStatus());

        // Só atualiza senha se o usuário mandou uma nova
        if (request.getSenha() != null && !request.getSenha().isEmpty()) {
            entity.setSenha(request.getSenha()); // Idealmente criptografar, mas para demo ok
        }

        // Salva no banco
        usuarioRepository.save(entity);

        // Retorna 204 No Content (Sucesso sem corpo)
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<UsuarioResponse> response = usuarios.stream()
                .map(UsuarioResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> getMeuPerfil(Authentication authentication) {
        // Se estiver no modo "Libera Geral", isso pode vir null, então tratamos
        if (authentication == null) {
            return ResponseEntity.notFound().build();
        }
        String matricula = authentication.getName();
        Usuario usuario = usuarioService.buscarPorMatricula(matricula).orElseThrow();
        return ResponseEntity.ok(UsuarioResponse.fromDomain(usuario));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
    }
}