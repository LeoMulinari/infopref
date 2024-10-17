package com.example.infopref.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.infopref.models.Equipamento;
import com.example.infopref.models.DTO.AlterarDepartamentoDTO;
import com.example.infopref.models.DTO.EquipamentoDTO;
import com.example.infopref.services.Equip_depService;
import com.example.infopref.services.EquipamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipamentos") // http://localhost:8080/equipamento
@Validated
public class EquipamentoController {
    @Autowired
    EquipamentoService equipamentoService;

    @Autowired
    Equip_depService equip_depService;

    @GetMapping
    public ResponseEntity<List<EquipamentoDTO>> getEquipamento() {
        List<EquipamentoDTO> equipamentosDTO = equipamentoService.findAllWithDataAquisicao();
        return ResponseEntity.ok().body(equipamentosDTO);
    }

    @GetMapping("/{id}") // http://localhost:8080/equipamento/2
    public ResponseEntity<Equipamento> getEquipamentoById(@PathVariable("id") Long id) {
        Equipamento obj = this.equipamentoService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/departamento/{departamentoId}")
    public ResponseEntity<List<EquipamentoDTO>> getEquipamentosByDepartamento(@PathVariable Long departamentoId) {
        List<EquipamentoDTO> equipamentos = equipamentoService.findByDepartamentoWithDataAquisicao(departamentoId);
        return ResponseEntity.ok(equipamentos);
    }

    @PostMapping("/equipamentodep")
    public ResponseEntity<Void> postEquipamento(
            @RequestBody @Valid EquipamentoDTO equipamentoDTO,
            @RequestParam Long departamentoId) {

        System.out.println(
                "POST Equipamento: Departamento ID = " + departamentoId + ", Data Aquisicao = "
                        + equipamentoDTO.getData_aquisicao());
        Equipamento obj = new Equipamento();
        // Configurar obj com os dados do DTO
        obj.setNum_patrimonio(equipamentoDTO.getNum_patrimonio());
        obj.setModelo(equipamentoDTO.getModelo());
        obj.setMarca(equipamentoDTO.getMarca());
        obj.setDescr_tec(equipamentoDTO.getDescr_tec());

        equipamentoService.create(obj, departamentoId, equipamentoDTO.getData_aquisicao());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putEquipamento(@PathVariable("id") Long id, @Valid @RequestBody Equipamento newObj) {
        System.out.println(newObj);
        System.out.println(id);
        newObj.setId(id);
        this.equipamentoService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/alterar-departamento")
    public ResponseEntity<Void> alterarDepartamento(
            @PathVariable("id") Long id,
            @RequestBody AlterarDepartamentoDTO dto) {
        equip_depService.alterarDepartamento(id, dto.getNovoDepartamentoId(), dto.getNovaDataAquisicao());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipamentoById(@PathVariable("id") Long id) {
        this.equipamentoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/equip_dep/equipamento/{id}")
    public ResponseEntity<Void> deleteEquipDepAssociationsByEquipamentoId(@PathVariable("id") Long id) {
        this.equip_depService.deleteAssociationsByEquipamentoId(id);
        return ResponseEntity.noContent().build();
    }

}
