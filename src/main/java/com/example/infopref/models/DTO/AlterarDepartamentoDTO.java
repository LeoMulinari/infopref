package com.example.infopref.models.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AlterarDepartamentoDTO {
    private Long novoDepartamentoId;
    private LocalDate novaDataAquisicao;
}
