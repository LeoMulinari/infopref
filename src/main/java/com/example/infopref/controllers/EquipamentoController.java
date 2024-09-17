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
import com.example.infopref.models.DTO.EquipamentoDTO;
import com.example.infopref.services.EquipamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipamentos") // http://localhost:8080/equipamento
@Validated
public class EquipamentoController {
    @Autowired
    EquipamentoService equipamentoService;

    @GetMapping
    public ResponseEntity<List<Equipamento>> getEquipamento() {
        return ResponseEntity.ok().body(equipamentoService.findAll());
    }

    @GetMapping("/{id}") // http://localhost:8080/equipamento/2
    public ResponseEntity<Equipamento> getEquipamentoById(@PathVariable("id") Long id) {
        Equipamento obj = this.equipamentoService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/departamento/{id}")
    public ResponseEntity<List<Equipamento>> getEquipamentosByDepartamento(
            @PathVariable("id") Long id) {
        List<Equipamento> equipamentos = equipamentoService.findByDepartamento(id);
        return ResponseEntity.ok().body(equipamentos);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipamentoById(@PathVariable("id") Long id) {
        this.equipamentoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
