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

import com.example.infopref.models.Departamento;
import com.example.infopref.services.DepartamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/departamento") // http://localhost:8080/departamento
@Validated
public class DepartamentoController {
    @Autowired
    DepartamentoService departamentoService;

    @GetMapping("/{id}") // http://localhost:8080/departamento/2
    public ResponseEntity<Departamento> getDepartamento(@PathVariable("id") Long id) {
        Departamento obj = this.departamentoService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/secretaria/{cod_sec}")
    public ResponseEntity<List<Departamento>> getAllDepartamentoByCod_sec(@PathVariable("cod_sec") Long cod_sec) {
        List<Departamento> obj = this.departamentoService.findAllByCod_sec(cod_sec);

        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> postDepartamento(@RequestBody @Valid Departamento obj) {
        this.departamentoService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putDepartamento(@PathVariable("id") Long id, @Valid @RequestBody Departamento newObj) {
        newObj.setId(id);
        this.departamentoService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamentoById(@PathVariable("id") Long id) {
        this.departamentoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
