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

import com.example.infopref.models.Tecnico;
import com.example.infopref.services.TecnicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tecnicos") // http://localhost:8080/tecnico
@Validated
public class TecnicoController {
    @Autowired
    TecnicoService tecnicoService;

    @GetMapping
    public ResponseEntity<List<Tecnico>> getTecnico() {
        return ResponseEntity.ok().body(tecnicoService.findAll());
    }

    @GetMapping("/{id}") // http://localhost:8080/tecnico/2
    public ResponseEntity<Tecnico> getTecnicoById(@PathVariable("id") Long id) {
        Tecnico obj = this.tecnicoService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Tecnico> postTecnico(@RequestBody @Valid Tecnico obj) {
        System.out.println("Recebendo o técnico: " + obj);
        // Verificar se o cod_usuario é válido
        if (obj.getUser().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        this.tecnicoService.create(obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putTecnico(@PathVariable("id") Long id, @Valid @RequestBody Tecnico newObj) {
        newObj.setId(id);
        this.tecnicoService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTecnicoById(@PathVariable("id") Long id) {
        this.tecnicoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
