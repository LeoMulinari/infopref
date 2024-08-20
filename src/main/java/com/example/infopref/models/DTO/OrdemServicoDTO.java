package com.example.infopref.models.DTO;

import java.sql.Date;
import java.util.Map;
import java.util.Set;

import com.example.infopref.models.Enums.Prioridade;
import com.example.infopref.models.Enums.StatusOS;

import lombok.Data;

@Data
public class OrdemServicoDTO {
    private Long id;
    private String num_protocolo;
    private StatusOS status;
    private String tipo_chamado;
    private String descricao;
    private Prioridade prioridade;
    private String resolucao;
    private Date data_abertura;
    private Date data_finalizacao;
    private Long solicitanteId;
    private Long tecnicoId;
    private Long userId;
    private Set<Long> equipamentosIds;
    private Map<Long, Date> dataEntregaMap;
}
