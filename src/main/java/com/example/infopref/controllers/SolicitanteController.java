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

import com.example.infopref.models.Solicitante;
import com.example.infopref.services.SolicitanteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/solicitantes") // http://localhost:8080/solicitante
@Validated
public class SolicitanteController {
    @Autowired
    SolicitanteService solicitanteService;

    @GetMapping
    public ResponseEntity<List<Solicitante>> getSolicitante() {
        return ResponseEntity.ok().body(solicitanteService.findAll());
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<Solicitante> getSolicitanteByUserId(@PathVariable("userId") Long userId) {
        Solicitante obj = this.solicitanteService.findByUserId(userId);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/{id}") // http://localhost:8080/solicitante/2
    public ResponseEntity<Solicitante> getSolicitanteById(@PathVariable("id") Long id) {
        Solicitante obj = this.solicitanteService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/departamento/{cod_dep}")
    public ResponseEntity<List<Solicitante>> getAllSolicitanteByCod_dep(@PathVariable("cod_dep") Long cod_dep) {
        List<Solicitante> obj = this.solicitanteService.findAllByCod_dep(cod_dep);

        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> postSolicitante(@RequestBody @Valid Solicitante obj) {
        this.solicitanteService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putSolicitante(@PathVariable("id") Long id, @Valid @RequestBody Solicitante newObj) {
        newObj.setId(id);
        this.solicitanteService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitanteById(@PathVariable("id") Long id) {
        this.solicitanteService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
