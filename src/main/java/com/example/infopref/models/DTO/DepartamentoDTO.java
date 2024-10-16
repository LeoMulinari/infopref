package com.example.infopref.models.DTO;

import lombok.Data;

@Data
public class DepartamentoDTO {
    private Long id;
    private String nome;
    private String fone;
    private Long secretariaId;
}
