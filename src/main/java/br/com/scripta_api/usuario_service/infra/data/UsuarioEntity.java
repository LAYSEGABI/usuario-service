package br.com.scripta_api.usuario_service.infra.data;

import br.com.scripta_api.usuario_service.application.domain.TipoDeConta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity(name = "Usuarios")
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    @JsonIgnore // ADICIONE ISSO: Protege para a senha nunca vazar no JSON (e evita alguns loops)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeConta tipoDeConta;

    // --- NOVOS CAMPOS (Adicionados para bater com o seu Front-end) ---

    @Column(unique = true) // CPF deve ser único no sistema
    private String cpf;

    @Column(unique = true) // Email também costuma ser único
    private String email;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'ATIVO'")
    private String status; // Ex: "ATIVO", "PENDENTE"
}