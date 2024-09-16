package com.example.infopref.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Departamento;
import com.example.infopref.models.Equip_dep;
import com.example.infopref.models.Equipamento;
import com.example.infopref.repositories.EquipamentoRepository;

@Service
public class EquipamentoService {
    @Autowired
    EquipamentoRepository equipamentoRepository;

    @Autowired
    DepartamentoService departamentoService;

    @Autowired
    @Lazy
    Equip_depService equip_depService;

    @Autowired
    UserService userService;

    public List<Equipamento> findAll() {
        return equipamentoRepository.findAll();
    }

    public List<Equipamento> findByDepartamento(Long id) {
        return equipamentoRepository.findByDepartamento(id);
    }

    public Equipamento findById(Long id) {
        userService.VerificaADMeTec();
        Optional<Equipamento> obj = this.equipamentoRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Equipamento não encontrado {id:" + id + "}");
    }

    public Equipamento create(Equipamento obj, Long departamentoId, Date dataAquisicao) {
        userService.VerificaADMeTec();
        obj.setId(null);
        System.out.println(
                "Criando Equipamento: Departamento ID = " + departamentoId + ", Data Aquisicao = " + dataAquisicao);
        Equipamento savedEquipamento = this.equipamentoRepository.save(obj);

        // Associar o equipamento ao departamento
        Departamento departamento = departamentoService.findById(departamentoId);

        Equip_dep equipDep = new Equip_dep();
        equipDep.setEquipamento(savedEquipamento);
        equipDep.setDepartamento(departamento);
        equipDep.setData_aquisicao(dataAquisicao);

        equip_depService.create(equipDep);

        return savedEquipamento;
    }

    public Equipamento update(Equipamento newObj) {
        Equipamento obj = this.findById(newObj.getId());

        obj.setNum_patrimonio(newObj.getNum_patrimonio());
        obj.setModelo(newObj.getModelo());
        obj.setMarca(newObj.getMarca());
        obj.setDescr_tec(newObj.getDescr_tec());

        return this.equipamentoRepository.save(obj);
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            this.equipamentoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar equipamento {id:" + id + "}", e);
        }
    }
}
