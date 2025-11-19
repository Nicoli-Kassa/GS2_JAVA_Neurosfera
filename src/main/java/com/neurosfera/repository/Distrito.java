package com.neurosfera.repository;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "DISTRITOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Distrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DISTRITO")
    private Long idDistrito;

    @NotBlank(message = "Nome do distrito é obrigatório")
    @Size(min = 3, max = 100)
    @Column(name = "NM_DISTRITO", nullable = false, unique = true, length = 100)
    private String nmDistrito;

    @Pattern(regexp = "Medicina|Engenharia|Design|IA|Sustentabilidade|Negócios|Criatividade",
            message = "Área inválida")
    @NotBlank(message = "Área do distrito é obrigatória")
    @Column(name = "AREA_DISTRITO", nullable = false, length = 100)
    private String areaDistrito;

    @Column(name = "DS_DISTRITO", columnDefinition = "TEXT")
    private String dsDistrito;

    @Column(name = "ST_DISTRITO", nullable = false)
    private Boolean stDistrito = true;

    @ManyToMany(mappedBy = "distritosInscritos")
    @JsonBackReference
    private Set<Usuario> usuariosInscritos = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Distrito)) return false;
        Distrito distrito = (Distrito) o;
        return idDistrito != null && idDistrito.equals(distrito.getIdDistrito());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
