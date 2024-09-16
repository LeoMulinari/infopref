package com.example.infopref.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Departamento;
import com.example.infopref.models.Equip_dep;
import com.example.infopref.models.Equipamento;
import com.example.infopref.repositories.Equip_depRepository;

@Service
public class Equip_depService {

    @Autowired
    Equip_depRepository equip_depRepository;

    @Autowired
    DepartamentoService departamentoService;

    @Autowired
    EquipamentoService equipamentoService;

    @Autowired
    UserService userService;

    public Equip_dep findByEquipamento_IdAndDepartamento_Id(Long equipamento_id, Long departamento_id) {
        userService.VerificaADMeTec();
        Optional<Equip_dep> obj = this.equip_depRepository.findByEquipamento_IdAndDepartamento_Id(equipamento_id,
                departamento_id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Equip_dep não encontrado {id:" + equipamento_id + departamento_id + "}");
    }

    public List<Equip_dep> findAllByCod_dep(Long cod_dep) {
        userService.VerificaADMeTec();
        this.departamentoService.findById(cod_dep);
        List<Equip_dep> listDep = this.equip_depRepository.findAllByDepartamento_Id(cod_dep);

        return listDep;
    }

    public Equip_dep create(Equip_dep obj) {
        userService.VerificaADMeTec();

        System.out.println("Associando Equipamento ID = " + obj.getEquipamento().getId() +
                " com Departamento ID = " + obj.getDepartamento().getId() +
                ", Data Aquisicao = " + obj.getData_aquisicao());

        Equipamento equipamento = equipamentoService.findById(obj.getEquipamento().getId());
        Departamento departamento = departamentoService.findById(obj.getDepartamento().getId());
        obj.setEquipamento(equipamento);
        obj.setDepartamento(departamento);

        return this.equip_depRepository.save(obj);
    }

    public Equip_dep update(Equip_dep newObj) {
        userService.VerificaADMeTec();
        Equipamento equipamento = equipamentoService.findById(newObj.getEquipamento().getId());
        Departamento departamento = departamentoService.findById(newObj.getDepartamento().getId());

        Equip_dep obj = this.findByEquipamento_IdAndDepartamento_Id(equipamento.getId(), departamento.getId());

        obj.setData_aquisicao(newObj.getData_aquisicao());

        return this.equip_depRepository.save(obj);
    }

    public void deleteByEquipamento_IdAndDepartamento_Id(Long equipamento_id, Long departamento_id) {
        findByEquipamento_IdAndDepartamento_Id(equipamento_id, departamento_id);
        try {
            this.equip_depRepository.deleteByEquipamento_IdAndDepartamento_Id(equipamento_id, departamento_id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar equip_dep {id:" + equipamento_id + departamento_id + "}", e);
        }
    }
}
