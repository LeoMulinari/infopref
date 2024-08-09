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

import com.example.infopref.models.Equip_os;
import com.example.infopref.services.Equip_osService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/equip_os") // http://localhost:8080/equip_os
@Validated
public class Equip_osController {
    @Autowired
    Equip_osService equip_osService;

    @GetMapping("/{id}") // http://localhost:8080/equip_os/2
    public ResponseEntity<Equip_os> getEquip_os(@PathVariable("id") Long id) {
        Equip_os obj = this.equip_osService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/departamento/{cod_dep}")
    public ResponseEntity<List<Equip_os>> getAllEquip_osByCod_os(@PathVariable("cod_os") Long cod_os) {
        List<Equip_os> obj = this.equip_osService.findAllByCod_os(cod_os);

        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> postEquip_os(@RequestBody @Valid Equip_os obj) {
        this.equip_osService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getEquipamento())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putEquip_os(@PathVariable("id") Long id, @Valid @RequestBody Equip_os newObj) {
        newObj.setEquipamento(null);
        ;
        this.equip_osService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquip_osById(@PathVariable("id") Long id) {
        this.equip_osService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
