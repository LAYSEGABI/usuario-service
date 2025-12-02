package br.com.scripta_api.usuario_service.infra.data;

import br.com.scripta_api.usuario_service.application.domain.TipoDeConta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonIgnore // Mantém o json limpo
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeConta tipoDeConta;

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'ATIVO'")
    private String status;

    // REMOVI: CPF, Email, DataNascimento para evitar erros na apresentação.
}