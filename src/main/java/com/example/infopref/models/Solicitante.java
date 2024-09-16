package com.example.infopref.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = Solicitante.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Solicitante {
    public static final String TABLENAME = "solicitante";

    @Id
    @Column(name = "cod_sol")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", unique = false, nullable = false, updatable = true, length = 100) // rever update
    @NotBlank
    private String nome;

    @Column(name = "fone", unique = false, nullable = false, updatable = true)
    @NotBlank
    @Size(min = 14, max = 15)
    private String fone;

    @Column(name = "id_acesso_remoto", unique = true, nullable = false, updatable = true) // rever update
    @NotNull
    private Long id_acesso_remoto;

    @ManyToOne
    @JoinColumn(name = "cod_usuario", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "cod_dep", nullable = false)
    private Departamento departamento;
}
