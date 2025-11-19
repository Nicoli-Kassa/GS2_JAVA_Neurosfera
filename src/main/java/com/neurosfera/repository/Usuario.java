package com.neurosfera.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USUARIOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @NotBlank(message = "Nome do usuário é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(name = "NM_USUARIO", nullable = false, length = 100)
    private String nmUsuario;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(name = "EMAIL_USUARIO", nullable = false, unique = true, length = 150)
    private String emailUsuario;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Column(name = "SENHA_USUARIO", nullable = false, length = 255)
    private String senhaUsuario;

    @NotNull(message = "Tipo de usuário é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "TP_USUARIO", nullable = false, length = 50)
    private TipoUsuario tpUsuario;

    @Column(name = "DT_CADASTRO_USUARIO", updatable = false)
    private LocalDate dtCadastroUsuario;

    @Column(name = "IDIOMA_USUARIO", length = 10)
    private String idiomaUsuario = "PT-BR";

    @Column(name = "ST_USUARIO", nullable = false)
    private Boolean stUsuario = true;

    @ManyToMany
    @JoinTable(
            name = "USUARIO_DISTRITO",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_DISTRITO")
    )
    @JsonIgnore
    private Set<Distrito> distritosInscritos = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (dtCadastroUsuario == null) {
            dtCadastroUsuario = LocalDate.now();
        }
        if (stUsuario == null) {
            stUsuario = true;
        }
        if (idiomaUsuario == null) {
            idiomaUsuario = "PT-BR";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return idUsuario != null && idUsuario.equals(usuario.getIdUsuario());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
