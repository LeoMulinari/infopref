package com.example.infopref.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.OrdemServico;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    List<OrdemServico> findAllByTecnico_Id(Long cod_tec);

    List<OrdemServico> findAllBySolicitante_Id(Long cod_sol);

}
