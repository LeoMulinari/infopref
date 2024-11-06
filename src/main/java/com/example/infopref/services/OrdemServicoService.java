package com.example.infopref.services;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.OrdemServico;
import com.example.infopref.models.DTO.OrdemServicoDTO;
import com.example.infopref.models.Enums.Prioridade;
import com.example.infopref.models.Enums.StatusOS;
import com.example.infopref.models.Enums.TipoUser;
import com.example.infopref.repositories.EquipamentoRepository;
import com.example.infopref.repositories.OrdemServicoRepository;
import com.example.infopref.repositories.SolicitanteRepository;
import com.example.infopref.repositories.TecnicoRepository;
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
    UserService userService;

    @Autowired
    EquipamentoRepository equipamentoRepository;

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
        this.solicitanteRepository.findById(cod_sol);
        List<OrdemServico> listSol = this.ordemServicoRepository.findAllBySolicitante_Id(cod_sol);

        return listSol;
    }

    public Long countByStatus(StatusOS status) {
        return ordemServicoRepository.countByStatus(status);
    }

    public Long countByPriority(Prioridade prioridade) {
        return ordemServicoRepository.countByPrioridade(prioridade);
    }

    public Long countFinalizedThisMonth() {
        YearMonth currentMonth = YearMonth.now();
        return ordemServicoRepository.countByStatusAndMonth(StatusOS.FINALIZADO, currentMonth.getYear(),
                currentMonth.getMonthValue());
    }

    public Long countUrgentPriorityNotFinalized() {
        return ordemServicoRepository.countUrgentNotFinalized(Prioridade.Urgente, StatusOS.FINALIZADO);
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

        if (dto.getResolucao() != null) {
            ordemServico.setResolucao(dto.getResolucao());
        }

        ordemServico.setSolicitante(solicitanteRepository.findById(dto.getCod_sol()).orElseThrow());
        if (dto.getCod_tec() != null) {
            ordemServico.setTecnico(tecnicoRepository.findById(dto.getCod_tec()).orElseThrow());
        }

        if (dto.getEquipamentoPatrimonio() != null && !dto.getEquipamentoPatrimonio().isEmpty()) {
            ordemServico.setEquipamentoPatrimonio(String.join(", ", dto.getEquipamentoPatrimonio()));
        }

        return ordemServicoRepository.save(ordemServico);
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

        if (dto.getEquipamentoPatrimonio() != null && !dto.getEquipamentoPatrimonio().isEmpty()) {
            obj.setEquipamentoPatrimonio(String.join(", ", dto.getEquipamentoPatrimonio()));
        }

        obj = ordemServicoRepository.save(obj);

        return obj;
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            this.ordemServicoRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar ordemServico {id:" + id + "}", e);
        }
    }
}
