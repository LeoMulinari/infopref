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

import com.example.infopref.models.Secretaria;
import com.example.infopref.services.SecretariaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/secretaria") // http://localhost:8080/secretaria
@Validated
public class SecretariaController {
    @Autowired
    SecretariaService secretariaService;

    @GetMapping
    public ResponseEntity<List<Secretaria>> getSecretaria() {
        return ResponseEntity.ok().body(secretariaService.findAll());
    }

    @GetMapping("/{id}") // http://localhost:8080/secretaria/2
    public ResponseEntity<Secretaria> getSecretariaById(@PathVariable("id") Long id) {
        Secretaria obj = this.secretariaService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> postSecretaria(@RequestBody @Valid Secretaria obj) {
        this.secretariaService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putSecretaria(@PathVariable("id") Long id, @Valid @RequestBody Secretaria newObj) {
        newObj.setId(id);
        this.secretariaService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSecretariaById(@PathVariable("id") Long id) {
        this.secretariaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
