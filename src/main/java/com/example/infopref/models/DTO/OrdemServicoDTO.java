package com.example.infopref.models.DTO;

import java.sql.Date;
import java.util.List;

import com.example.infopref.models.Enums.Prioridade;
import com.example.infopref.models.Enums.StatusOS;

import lombok.Data;

@Data
public class OrdemServicoDTO {
    private String num_protocolo;
    private StatusOS status;
    private String tipo_chamado;
    private String descricao;
    private Prioridade prioridade;
    private Date data_abertura;
    private Date data_finalizacao;
    private Long solicitanteId;
    private Long tecnicoId;
    private Long userId;
    private List<Long> equipamentosIds;
}
