package com.example.infopref.models.DTO;

import java.time.LocalDate;
import java.util.List;

import com.example.infopref.models.Enums.Prioridade;
import com.example.infopref.models.Enums.StatusOS;
import com.example.infopref.models.Enums.TipoChamado;

import lombok.Data;

@Data
public class OrdemServicoDTO {
    private Long id;
    private StatusOS status;
    private TipoChamado tipo_chamado;
    private String descricao;
    private Prioridade prioridade;
    private String resolucao;
    private LocalDate data_abertura;
    private LocalDate data_finalizacao;
    private Long cod_sol;
    private Long cod_tec;
    private List<Long> equipamentosIds;
    private String equipamentoPatrimonio;
}
