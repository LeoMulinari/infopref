package com.example.infopref.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Equip_os;
import com.example.infopref.repositories.Equip_osRepository;

@Service
public class Equip_osService {
    @Autowired
    Equip_osRepository equip_osRepository;

    @Autowired
    OrdemServicoService ordemServicoService;

    public Equip_os findById(Long id) {
        Optional<Equip_os> obj = this.equip_osRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Equip_os não encontrado {id:" + id + "}");
    }

    public List<Equip_os> findAllByCod_os(Long cod_os) {
        this.ordemServicoService.findById(cod_os);
        List<Equip_os> listOs = this.equip_osRepository.findAllByOrdemServico_Id(cod_os);

        return listOs;
    }

    public Equip_os create(Equip_os obj) {
        obj.setEquipamento(null);

        return this.equip_osRepository.save(obj);
    }

    public Equip_os update(Equip_os newObj) {
        Equip_os obj = this.findById(newObj.getEquipamento().getId());

        obj.setData_entrega(newObj.getData_entrega());

        return this.equip_osRepository.save(obj);
    }

    public void deleteById(Long id) {
        try {
            this.equip_osRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar equip_os {id:" + id + "}", e);
        }
    }
}
