package com.example.infopref.models.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EquipamentoDTO {
    private Long id;
    private String num_patrimonio;
    private String modelo;
    private String marca;
    private String descr_tec;
    private LocalDate data_aquisicao;
}
