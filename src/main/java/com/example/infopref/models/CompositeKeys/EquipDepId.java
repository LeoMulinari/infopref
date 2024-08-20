package com.example.infopref.models.CompositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipDepId implements java.io.Serializable {
    @Column(name = "cod_dep")
    private Long departamentoId;

    @Column(name = "cod_equip")
    private Long equipamentoId;
}
