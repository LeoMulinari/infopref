package com.example.infopref.models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Equip_os.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Equip_os {
    public static final String TABLENAME = "equip_os";

    @Id
    @Column(name = "cod_equip_os")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_entrega", unique = false, nullable = false, updatable = true)
    @NotNull
    private Date data_entrega;

    @ManyToOne
    @JoinColumn(name = "cod_equip", nullable = false)
    private Equipamento equipamento;

    @ManyToOne
    @JoinColumn(name = "cod_os", nullable = false)
    private OrdemServico ordemServico;

}
