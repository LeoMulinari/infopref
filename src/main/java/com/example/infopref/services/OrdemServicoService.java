package com.example.infopref.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.OrdemServico;
import com.example.infopref.repositories.OrdemServicoRepository;

@Service
public class OrdemServicoService {
    @Autowired
    OrdemServicoRepository ordemServicoRepository;

    @Autowired
    TecnicoService tecnicoService;

    @Autowired
    SolicitanteService solicitanteService;

    public OrdemServico findById(Long id) {
        Optional<OrdemServico> obj = this.ordemServicoRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Ordem de Serviço não encontrada {id:" + id + "}");
    }

    public List<OrdemServico> findAllByCod_tec(Long cod_tec) {
        this.tecnicoService.findById(cod_tec);
        List<OrdemServico> listTec = this.ordemServicoRepository.findAllByTecnico_Id(cod_tec);

        return listTec;
    }

    public List<OrdemServico> findAllByCod_sol(Long cod_sol) {
        this.solicitanteService.findById(cod_sol);
        List<OrdemServico> listSol = this.ordemServicoRepository.findAllBySolicitante_Id(cod_sol);

        return listSol;
    }

    public OrdemServico create(OrdemServico obj) {
        obj.setId(null);

        return this.ordemServicoRepository.save(obj);
    }

    public OrdemServico update(OrdemServico newObj) {
        OrdemServico obj = this.findById(newObj.getId());

        obj.setStatus(newObj.getStatus());
        obj.setTipo_chamado(newObj.getTipo_chamado());
        obj.setDescricao(newObj.getDescricao());
        obj.setPrioridade(newObj.getPrioridade());
        obj.setResolucao(newObj.getResolucao());
        obj.setData_abertura(newObj.getData_abertura());
        obj.setData_finalizacao(newObj.getData_finalizacao());

        return this.ordemServicoRepository.save(obj);
    }

    public void deleteById(Long id) {
        try {
            this.ordemServicoRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar ordemServico {id:" + id + "}", e);
        }
    }
}
