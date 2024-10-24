package com.example.infopref.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Equip_dep.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Equip_dep {
    public static final String TABLENAME = "equip_dep";

    @Id
    @Column(name = "cod_equip_dep")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_aquisicao", unique = false, nullable = true, updatable = true)
    private LocalDate data_aquisicao;

    @ManyToOne
    @JoinColumn(name = "cod_equip", nullable = false)
    private Equipamento equipamento;

    @ManyToOne
    @JoinColumn(name = "cod_dep", nullable = false)
    private Departamento departamento;
}
