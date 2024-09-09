package com.example.infopref.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Equip_os;
import com.example.infopref.models.Equipamento;
import com.example.infopref.models.OrdemServico;
import com.example.infopref.repositories.Equip_osRepository;

@Service
public class Equip_osService {
    @Autowired
    Equip_osRepository equip_osRepository;

    @Autowired
    OrdemServicoService ordemServicoService;

    @Autowired
    EquipamentoService equipamentoService;

    @Autowired
    UserService userService;

    public Equip_os findByEquipamento_IdAndOrdemServico_Id(Long equipamento_id, Long ordemServico_id) {
        userService.VerificaADMeTec();
        Optional<Equip_os> obj = this.equip_osRepository.findByEquipamento_IdAndOrdemServico_Id(equipamento_id,
                ordemServico_id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Equip_os não encontrado {id:" + equipamento_id + ordemServico_id + "}");
    }

    public List<Equip_os> findAllByCod_os(Long cod_os) {
        this.ordemServicoService.findById(cod_os);
        List<Equip_os> listOs = this.equip_osRepository.findAllByOrdemServico_Id(cod_os);

        return listOs;
    }

    public Equip_os create(Equip_os obj) {
        Equipamento equipamento = equipamentoService.findById(obj.getEquipamento().getId());
        OrdemServico ordemServico = ordemServicoService.findById(obj.getOrdemServico().getId());
        obj.setEquipamento(equipamento);
        obj.setOrdemServico(ordemServico);

        return this.equip_osRepository.save(obj);
    }

    public Equip_os update(Equip_os newObj) {
        Equipamento equipamento = equipamentoService.findById(newObj.getEquipamento().getId());
        OrdemServico ordemServico = ordemServicoService.findById(newObj.getOrdemServico().getId());

        Equip_os obj = this.findByEquipamento_IdAndOrdemServico_Id(equipamento.getId(), ordemServico.getId());

        obj.setData_entrega(newObj.getData_entrega());

        return this.equip_osRepository.save(obj);
    }

    public void deleteByEquipamento_IdAndOrdemServico_Id(Long equipamento_id, Long ordemServico_id) {
        findByEquipamento_IdAndOrdemServico_Id(equipamento_id, ordemServico_id);
        try {
            this.equip_osRepository.deleteByEquipamento_IdAndOrdemServico_Id(equipamento_id, ordemServico_id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar equip_os {id:" + equipamento_id + ordemServico_id + "}", e);
        }
    }
}
