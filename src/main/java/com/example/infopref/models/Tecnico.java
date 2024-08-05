package com.example.infopref.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Tecnico.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tecnico {
    public static final String TABLENAME = "tecnico";

    @Id
    @Column(name = "cod_tec")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", unique = false, nullable = false, updatable = true, length = 100) // rever update
    @NotBlank
    private String nome;

    @Column(name = "fone", unique = false, nullable = false, updatable = true)
    @NotBlank
    @Size(min = 11, max = 11)
    private String fone;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "tipo", unique = false, nullable = false, updatable = false)
    private TipoTecnico tipo;

    @ManyToOne
    @JoinColumn(name = "cod_usuario", nullable = false)
    private User user;
}
