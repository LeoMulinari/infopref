package com.example.infopref.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.OrdemServico;
import com.example.infopref.models.DTO.OrdemServicoDTO;
import com.example.infopref.models.Enums.TipoUser;
import com.example.infopref.repositories.Equip_osRepository;
import com.example.infopref.repositories.EquipamentoRepository;
import com.example.infopref.repositories.OrdemServicoRepository;
import com.example.infopref.repositories.SolicitanteRepository;
import com.example.infopref.repositories.TecnicoRepository;
import com.example.infopref.repositories.UserRepository;
import com.example.infopref.security.UserSpringSecurity;
import com.example.infopref.services.exceptions.AuthorizationException;

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

    @Autowired
    UserService userService;

    public List<OrdemServico> findAll() {
        return ordemServicoRepository.findAll();
    }

    public OrdemServico findById(Long id) {
        Optional<OrdemServico> obj = this.ordemServicoRepository.findById(id);
        if (obj.isEmpty()) {
            throw new RuntimeException("Ordem de Serviço não encontrada {id:" + id + "}");
        }
        UserSpringSecurity userSpringSecurity = userService.authenticated();
        if (!Objects.nonNull(userSpringSecurity) ||
                !id.equals(obj.get().getSolicitante().getId()) &&
                        !userSpringSecurity.hasRole(TipoUser.ADM) && !userSpringSecurity.hasRole(TipoUser.TECNICO))
            throw new AuthorizationException("Acesso negado!");
        return obj.get();

    }

    public List<OrdemServico> findAllByCod_tec(Long cod_tec) {
        userService.VerificaADMeTec();
        this.tecnicoRepository.findById(cod_tec);
        List<OrdemServico> listTec = this.ordemServicoRepository.findAllByTecnico_Id(cod_tec);

        return listTec;
    }

    public List<OrdemServico> findAllByCod_sol(Long cod_sol) {
        // userService.VerificaADMeTec();
        this.solicitanteRepository.findById(cod_sol);
        List<OrdemServico> listSol = this.ordemServicoRepository.findAllBySolicitante_Id(cod_sol);

        return listSol;
    }

    @Transactional
    public OrdemServico create(OrdemServicoDTO dto) {
        UserSpringSecurity userSpringSecurity = userService.authenticated();
        if (!Objects.nonNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setStatus(dto.getStatus());
        ordemServico.setTipo_chamado(dto.getTipo_chamado());
        ordemServico.setDescricao(dto.getDescricao());
        ordemServico.setPrioridade(dto.getPrioridade());
        ordemServico.setData_abertura(dto.getData_abertura());

        if (dto.getData_finalizacao() != null) {
            ordemServico.setData_finalizacao(dto.getData_finalizacao());
        }
        /*
         * if (dto.getData_abertura().after(dto.getData_finalizacao())) {
         * throw new
         * RuntimeException("A data de abertura não pode ser posterior à data de finalização."
         * );
         * }
         */

        // Setando as entidades relacionadas
        ordemServico.setSolicitante(solicitanteRepository.findById(dto.getCod_sol()).orElseThrow());

        // Apenas seta o técnico se o dto contiver o cod_tec
        if (dto.getCod_tec() != null) {
            ordemServico.setTecnico(tecnicoRepository.findById(dto.getCod_tec()).orElseThrow());
        }

        return ordemServicoRepository.save(ordemServico);

        // Buscando e associando os equipamentos
        /*
         * List<Equipamento> equipamentos = (List<Equipamento>) equipamentoRepository
         * .findAllById(dto.getEquipamentosIds());
         * ordemServico.setEquipamentos(equipamentos);
         */

    }

    @Transactional
    public OrdemServico update(OrdemServicoDTO dto) {
        userService.VerificaADMeTec();
        OrdemServico obj = this.findById(dto.getId());

        obj.setStatus(dto.getStatus());
        obj.setTipo_chamado(dto.getTipo_chamado());
        obj.setDescricao(dto.getDescricao());
        obj.setPrioridade(dto.getPrioridade());
        obj.setResolucao(dto.getResolucao());
        obj.setData_abertura(dto.getData_abertura());
        obj.setData_finalizacao(dto.getData_finalizacao());

        if (dto.getCod_sol() != null) {
            obj.setSolicitante(solicitanteRepository.findById(dto.getCod_sol()).orElseThrow());
        }

        if (dto.getCod_tec() != null) {
            obj.setTecnico(tecnicoRepository.findById(dto.getCod_tec()).orElseThrow());
        }

        // Atualiza os equipamentos e suas datas de entrega
        // Primeiramente, salvamos a ordem de serviço
        obj = ordemServicoRepository.save(obj);

        // Atualiza ou cria os registros equip_os com as datas de entrega
        /*
         * for (Map.Entry<Long, Date> entry : dto.getDataEntregaMap().entrySet()) {
         * Long equipamentoId = entry.getKey();
         * Date dataEntrega = entry.getValue();
         * 
         * // Encontra o Equipamento
         * Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
         * .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));
         * 
         * // Encontra ou cria o Equip_os correspondente
         * Equip_os equipOs = equipOsRepository.findByOrdemServicoAndEquipamento(obj,
         * equipamento)
         * .orElse(new Equip_os());
         * 
         * // Atualiza a data de entrega
         * equipOs.setData_entrega(dataEntrega);
         * equipOs.setEquipamento(equipamento);
         * equipOs.setOrdemServico(obj);
         * 
         * // Salva a instância de Equip_os
         * equipOsRepository.save(equipOs);
         * }
         */

        return obj;

    }

    public void deleteById(Long id) {
        // userService.VerificaADMeTec();
        findById(id);
        try {
            this.ordemServicoRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar ordemServico {id:" + id + "}", e);
        }
    }
}
