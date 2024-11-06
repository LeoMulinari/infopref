package com.example.infopref.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Equipamento.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Equipamento {
    public static final String TABLENAME = "equipamento";

    @Id
    @Column(name = "cod_equip")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_patrimonio", unique = true, nullable = false, updatable = false, length = 20)
    @NotBlank
    private String num_patrimonio;

    @Column(name = "modelo", unique = false, nullable = true, updatable = true, length = 30)
    private String modelo;

    @Column(name = "marca", unique = false, nullable = true, updatable = true, length = 30)
    private String marca;

    @Column(name = "descr_tec", unique = false, nullable = true, updatable = true, length = 255)
    private String descr_tec = "Não especificada"; // Valor padrão

    public void setDescr_tec(String descr_tec) {
        this.descr_tec = (descr_tec == null || descr_tec.isEmpty()) ? "Não especificada" : descr_tec;
    }

    @ManyToMany(mappedBy = "equipamentos")
    @JsonIgnore // Ignora a serialização dos equipamentos dentro de OrdemServico para evitar a
                // recursão
    private List<OrdemServico> ordemServicos;

    @ManyToMany(mappedBy = "equipamentos")
    @JsonIgnore // Ignora a serialização dos equipamentos dentro de OrdemServico para evitar a
                // recursão
    private List<Departamento> departamentos;
}
