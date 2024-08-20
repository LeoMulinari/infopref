package com.example.infopref.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.example.infopref.models.Departamento;
import com.example.infopref.models.DTO.DepartamentoDTO;
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
    public ResponseEntity<Departamento> postDepartamento(@RequestBody @Valid DepartamentoDTO departamentoDTO) {
        Departamento departamento = departamentoService.create(departamentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(departamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Departamento> update(@RequestBody DepartamentoDTO dto) {
        Departamento departamento = departamentoService.update(dto);
        return new ResponseEntity<>(departamento, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamentoById(@PathVariable("id") Long id) {
        this.departamentoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
