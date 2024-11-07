package com.example.infopref.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Secretaria.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Secretaria {
    public static final String TABLENAME = "secretaria";

    @Id
    @Column(name = "cod_sec")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", unique = true, nullable = false, updatable = true, length = 50)
    @NotBlank
    private String nome;

    @Column(name = "fone", unique = false, nullable = true, updatable = true)
    @Size(min = 10, max = 11)
    private String fone;
}
