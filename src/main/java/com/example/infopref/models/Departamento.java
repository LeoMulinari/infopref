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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Departamento.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Departamento {
    public static final String TABLENAME = "departamento";

    @Id
    @Column(name = "cod_dep")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", unique = false, nullable = false, updatable = true, length = 50) // rever update
    @NotBlank
    private String nome;

    @Column(name = "fone", unique = false, nullable = false, updatable = true)
    @NotBlank
    @Size(min = 11, max = 11)
    private String fone;

    @ManyToOne
    @JoinColumn(name = "cod_sec", nullable = false)
    private Secretaria secretaria;
}
