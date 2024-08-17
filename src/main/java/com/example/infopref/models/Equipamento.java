package com.example.infopref.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "equipamento")
    Set<Equip_os> equipamentos;

    @Column(name = "num_patrimonio", unique = true, nullable = false, updatable = false, length = 20)
    @NotBlank
    private String num_patrimonio;

    @Column(name = "modelo", unique = false, nullable = false, updatable = true, length = 30)
    @NotBlank
    private String modelo;

    @Column(name = "marca", unique = false, nullable = false, updatable = true, length = 30)
    @NotBlank
    private String marca;

    @Column(name = "descr_tec", unique = false, nullable = false, updatable = true, length = 255)
    private String descr_tec = "Não especificada"; // Valor padrão

    public void setDescr_tec(String descr_tec) {
        this.descr_tec = (descr_tec == null || descr_tec.isEmpty()) ? "Não especificada" : descr_tec;
    }
}
