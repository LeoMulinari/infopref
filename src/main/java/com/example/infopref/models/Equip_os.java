package com.example.infopref.models;

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

    @ManyToOne
    @JoinColumn(name = "cod_equip", nullable = false)
    private Equipamento equipamento;

    @ManyToOne
    @JoinColumn(name = "cod_os", nullable = false)
    private OrdemServico ordemServico;

}
