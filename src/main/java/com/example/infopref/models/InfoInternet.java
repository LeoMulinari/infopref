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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = InfoInternet.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InfoInternet {
    public static final String TABLENAME = "infoInternet";

    @Id
    @Column(name = "cod_infonet")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", unique = false, nullable = false, updatable = true, length = 30)
    @NotBlank
    private String nome;

    @Column(name = "senha", nullable = false, updatable = true, length = 30)
    @NotBlank
    private String senha;

    @Column(name = "ip", nullable = true, updatable = true, length = 16)
    private String ip;

    @ManyToOne
    @JoinColumn(name = "cod_dep", nullable = false)
    private Departamento departamento;
}
