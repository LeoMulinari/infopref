package com.example.infopref.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Equipamento;
import com.example.infopref.repositories.EquipamentoRepository;

@Service
public class EquipamentoService {
    @Autowired
    EquipamentoRepository equipamentoRepository;

    public Equipamento findById(Long id) {
        Optional<Equipamento> obj = this.equipamentoRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Equipamento não encontrado {id:" + id + "}");
    }

    public Equipamento create(Equipamento obj) {
        obj.setId(null);

        return this.equipamentoRepository.save(obj);
    }

    public Equipamento update(Equipamento newObj) {
        Equipamento obj = this.findById(newObj.getId());

        obj.setModelo(newObj.getModelo());
        obj.setMarca(newObj.getMarca());
        obj.setDescr_tec(newObj.getDescr_tec());

        return this.equipamentoRepository.save(obj);
    }

    public void deleteById(Long id) {
        try {
            this.equipamentoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar equipamento {id:" + id + "}", e);
        }
    }
}
