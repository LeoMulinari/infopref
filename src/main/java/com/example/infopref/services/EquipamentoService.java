package com.example.infopref.services;

import java.sql.Date;
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
        System.out.println(equipamentoRepository.findAllWithDepartamento());
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

        // obj.setNum_patrimonio(newObj.getNum_patrimonio());
        obj.setModelo(newObj.getModelo());
        obj.setMarca(newObj.getMarca());
        obj.setDescr_tec(newObj.getDescr_tec());

        return this.equipamentoRepository.save(obj);
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            // Remover todas as associações antes de excluir o equipamento
            equip_depService.deleteAssociationsByEquipamentoId(id);

            // Agora podemos excluir o equipamento
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

        // Obter a data de aquisição associada ao equipamento
        List<Equip_dep> equipDeps = equip_depService.findByEquipamentoId(equipamento.getId());
        if (!equipDeps.isEmpty()) {
            dto.setData_aquisicao(equipDeps.get(0).getData_aquisicao()); // Pega a data do primeiro associado
        }

        return dto;
    }

}
