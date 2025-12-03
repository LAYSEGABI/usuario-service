package br.com.scripta_api.usuario_service.controller;

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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    // --- ATENÇÃO: SÓ TEM O REPOSITÓRIO AQUI ---
    // (Não pode ter 'private final UsuarioService usuarioService;')
    private final UsuarioRepository usuarioRepository;

    // --- CRIAR ---
    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @RequestBody CriarUsuarioRequest request) {

        // Verifica se já existe
        if (usuarioRepository.existsByMatricula(request.getMatricula())) {
            throw new RuntimeException("Matrícula já cadastrada");
        }

        // CRIAÇÃO MANUAL (Sem Service)
        UsuarioEntity entity = new UsuarioEntity();
        entity.setNome(request.getNome());
        entity.setMatricula(request.getMatricula());
        entity.setTipoDeConta(request.getTipoDeConta());
        entity.setStatus(request.getStatus() != null ? request.getStatus() : "ATIVO");

        // Senha padrão se vier vazia
        String senha = (request.getSenha() != null && !request.getSenha().isEmpty()) ? request.getSenha() : "123456";
        entity.setSenha(senha);

        // Salva
        UsuarioEntity salvo = usuarioRepository.save(entity);

        // Converte para resposta manualmente para evitar erro de mapper
        UsuarioResponse response = new UsuarioResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getMatricula(),
                salvo.getTipoDeConta()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // --- LISTAR ---
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> response = usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getMatricula(), u.getTipoDeConta()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // --- PERFIL ---
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> getMeuPerfil(Authentication authentication) {
        if (authentication == null) return ResponseEntity.notFound().build();

        String matricula = authentication.getName();
        UsuarioEntity u = usuarioRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Usuario logado não encontrado"));

        return ResponseEntity.ok(new UsuarioResponse(u.getId(), u.getNome(), u.getMatricula(), u.getTipoDeConta()));
    }

    // --- DELETAR ---
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }
}