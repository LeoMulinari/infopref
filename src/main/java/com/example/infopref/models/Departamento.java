package com.example.infopref.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @Column(name = "nome", unique = true, nullable = false, updatable = true, length = 50) // rever update
    @NotBlank
    private String nome;

    @Column(name = "fone", unique = false, nullable = true, updatable = true)
    @Size(min = 14, max = 15)
    private String fone;

    @ManyToOne
    @JoinColumn(name = "cod_sec", nullable = false)
    private Secretaria secretaria;

    @ManyToMany
    @JoinTable(name = "equip_dep", joinColumns = @JoinColumn(name = "cod_dep"), inverseJoinColumns = @JoinColumn(name = "cod_equip"))
    private List<Equipamento> equipamentos = new ArrayList<>();
}
