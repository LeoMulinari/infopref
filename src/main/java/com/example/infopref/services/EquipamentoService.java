package com.example.infopref.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Departamento;
import com.example.infopref.models.Equip_dep;
import com.example.infopref.models.Equipamento;
import com.example.infopref.models.DTO.EquipamentoDTO;
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
        return equipamentoRepository.findAllWithDepartamento();
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

    public Equipamento create(Equipamento obj, Long departamentoId, LocalDate dataAquisicao) {
        userService.VerificaADMeTec();
        obj.setId(null);

        Equipamento savedEquipamento = this.equipamentoRepository.save(obj);

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

        obj.setModelo(newObj.getModelo());
        obj.setMarca(newObj.getMarca());
        obj.setDescr_tec(newObj.getDescr_tec());

        return this.equipamentoRepository.save(obj);
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            equip_depService.deleteAssociationsByEquipamentoId(id);

            equipamentoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar equipamento {id:" + id + "}", e);
        }
    }

    public List<EquipamentoDTO> findAllWithDataAquisicao() {
        List<Equipamento> equipamentos = equipamentoRepository.findAllWithDepartamento();
        return equipamentos.stream().map(this::convertToDTOWithDataAquisicao).collect(Collectors.toList());
    }

    public List<EquipamentoDTO> findByDepartamentoWithDataAquisicao(Long id) {
        List<Equipamento> equipamentos = equipamentoRepository.findByDepartamento(id);
        return equipamentos.stream().map(this::convertToDTOWithDataAquisicao).collect(Collectors.toList());
    }

    private EquipamentoDTO convertToDTOWithDataAquisicao(Equipamento equipamento) {
        EquipamentoDTO dto = new EquipamentoDTO();
        dto.setId(equipamento.getId());
        dto.setNum_patrimonio(equipamento.getNum_patrimonio());
        dto.setModelo(equipamento.getModelo());
        dto.setMarca(equipamento.getMarca());
        dto.setDescr_tec(equipamento.getDescr_tec());

        List<Equip_dep> equipDeps = equip_depService.findByEquipamentoId(equipamento.getId());
        if (!equipDeps.isEmpty()) {
            dto.setData_aquisicao(equipDeps.get(0).getData_aquisicao());
        }

        return dto;
    }

}
