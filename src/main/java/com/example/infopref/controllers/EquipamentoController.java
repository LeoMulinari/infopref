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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.infopref.models.Equipamento;
import com.example.infopref.services.EquipamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipamento") // http://localhost:8080/equipamento
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

    @PostMapping
    public ResponseEntity<Void> postEquipamento(@RequestBody @Valid Equipamento obj) {
        this.equipamentoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putEquipamento(@PathVariable("id") Long id, @Valid @RequestBody Equipamento newObj) {
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
