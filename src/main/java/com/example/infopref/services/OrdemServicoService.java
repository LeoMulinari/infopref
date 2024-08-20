package com.example.infopref.services;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Equip_os;
import com.example.infopref.models.Equipamento;
import com.example.infopref.models.OrdemServico;
import com.example.infopref.models.DTO.OrdemServicoDTO;
import com.example.infopref.repositories.Equip_osRepository;
import com.example.infopref.repositories.EquipamentoRepository;
import com.example.infopref.repositories.OrdemServicoRepository;
import com.example.infopref.repositories.SolicitanteRepository;
import com.example.infopref.repositories.TecnicoRepository;
import com.example.infopref.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrdemServicoService {
    @Autowired
    OrdemServicoRepository ordemServicoRepository;

    @Autowired
    TecnicoRepository tecnicoRepository;

    @Autowired
    SolicitanteRepository solicitanteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private Equip_osRepository equipOsRepository;

    public OrdemServico findById(Long id) {
        Optional<OrdemServico> obj = this.ordemServicoRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Ordem de Serviço não encontrada {id:" + id + "}");
    }

    public List<OrdemServico> findAllByCod_tec(Long cod_tec) {
        this.tecnicoRepository.findById(cod_tec);
        List<OrdemServico> listTec = this.ordemServicoRepository.findAllByTecnico_Id(cod_tec);

        return listTec;
    }

    public List<OrdemServico> findAllByCod_sol(Long cod_sol) {
        this.solicitanteRepository.findById(cod_sol);
        List<OrdemServico> listSol = this.ordemServicoRepository.findAllBySolicitante_Id(cod_sol);

        return listSol;
    }

    @Transactional
    public OrdemServico create(OrdemServicoDTO dto) {
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setNum_protocolo(dto.getNum_protocolo());
        ordemServico.setStatus(dto.getStatus());
        ordemServico.setTipo_chamado(dto.getTipo_chamado());
        ordemServico.setDescricao(dto.getDescricao());
        ordemServico.setPrioridade(dto.getPrioridade());
        ordemServico.setData_abertura(dto.getData_abertura());
        ordemServico.setData_finalizacao(dto.getData_finalizacao());

        // Setando as entidades relacionadas
        ordemServico.setSolicitante(solicitanteRepository.findById(dto.getSolicitanteId()).orElseThrow());
        ordemServico.setTecnico(tecnicoRepository.findById(dto.getTecnicoId()).orElseThrow());
        ordemServico.setUser(userRepository.findById(dto.getUserId()).orElseThrow());

        // Buscando e associando os equipamentos
        List<Equipamento> equipamentos = (List<Equipamento>) equipamentoRepository
                .findAllById(dto.getEquipamentosIds());
        ordemServico.setEquipamentos(equipamentos);

        return ordemServicoRepository.save(ordemServico);
    }

    @Transactional
    public OrdemServico update(OrdemServicoDTO dto) {
        OrdemServico obj = this.findById(dto.getId());

        obj.setStatus(dto.getStatus());
        obj.setTipo_chamado(dto.getTipo_chamado());
        obj.setDescricao(dto.getDescricao());
        obj.setPrioridade(dto.getPrioridade());
        obj.setResolucao(dto.getResolucao());
        obj.setData_abertura(dto.getData_abertura());
        obj.setData_finalizacao(dto.getData_finalizacao());

        // Atualiza os equipamentos e suas datas de entrega
        // Primeiramente, salvamos a ordem de serviço
        obj = ordemServicoRepository.save(obj);

        // Atualiza ou cria os registros equip_os com as datas de entrega
        for (Map.Entry<Long, Date> entry : dto.getDataEntregaMap().entrySet()) {
            Long equipamentoId = entry.getKey();
            Date dataEntrega = entry.getValue();

            // Encontra o Equipamento
            Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                    .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));

            // Encontra ou cria o Equip_os correspondente
            Equip_os equipOs = equipOsRepository.findByOrdemServicoAndEquipamento(obj, equipamento)
                    .orElse(new Equip_os());

            // Atualiza a data de entrega
            equipOs.setData_entrega(dataEntrega);
            equipOs.setEquipamento(equipamento);
            equipOs.setOrdemServico(obj);

            // Salva a instância de Equip_os
            equipOsRepository.save(equipOs);
        }

        return obj;

    }

    public void deleteById(Long id) {
        try {
            this.ordemServicoRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar ordemServico {id:" + id + "}", e);
        }
    }
}
