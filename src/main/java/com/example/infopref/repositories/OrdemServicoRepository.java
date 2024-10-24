package com.example.infopref.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.OrdemServico;
import com.example.infopref.models.Enums.TipoChamado;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
        List<OrdemServico> findAllByTecnico_Id(Long cod_tec);

        List<OrdemServico> findAllBySolicitante_Id(Long cod_sol);

        @Query("SELECT o FROM OrdemServico o " +
                        "LEFT JOIN o.solicitante s " +
                        "LEFT JOIN s.departamento d " +
                        "LEFT JOIN d.secretaria sec " +
                        "LEFT JOIN o.tecnico t " +
                        "WHERE o.data_abertura BETWEEN :dataInicio AND :dataFim AND " +
                        "(:tipoChamado IS NULL OR o.tipo_chamado = :tipoChamado) AND " +
                        "(:solicitanteId IS NULL OR s.id = :solicitanteId) AND " +
                        "(:departamentoId IS NULL OR d.id = :departamentoId) AND " +
                        "(:secretariaId IS NULL OR sec.id = :secretariaId) AND " +
                        "(:tecnicoId IS NULL OR t.id = :tecnicoId)")
        List<OrdemServico> findByDateRangeAndFilters(
                        @Param("dataInicio") Date dataInicio,
                        @Param("dataFim") Date dataFim,
                        @Param("tipoChamado") TipoChamado tipoChamado,
                        @Param("solicitanteId") Long solicitanteId,
                        @Param("departamentoId") Long departamentoId,
                        @Param("secretariaId") Long secretariaId,
                        @Param("tecnicoId") Long tecnicoId);
}
