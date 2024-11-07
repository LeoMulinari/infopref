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

import com.example.infopref.models.InfoInternet;
import com.example.infopref.services.InfoInternetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/infoInternet")
@Validated
public class InfoInternetController {
    @Autowired
    InfoInternetService infoInternetService;

    @GetMapping
    public ResponseEntity<List<InfoInternet>> getInfoInternet() {
        return ResponseEntity.ok().body(infoInternetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfoInternet> getInfoInternetById(@PathVariable("id") Long id) {
        InfoInternet obj = this.infoInternetService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/departamento/{cod_dep}")
    public ResponseEntity<List<InfoInternet>> getAllInfoInternetByCod_dep(@PathVariable("cod_dep") Long cod_dep) {
        List<InfoInternet> obj = this.infoInternetService.findAllByCod_dep(cod_dep);

        return ResponseEntity.ok().body(obj);
    }

    @PostMapping("/departamento/{cod_dep}")
    public ResponseEntity<Void> postInfoInternet(@PathVariable Long cod_dep, @RequestBody @Valid InfoInternet obj) {
        this.infoInternetService.create(obj, cod_dep);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putInfoInternet(@PathVariable("id") Long id, @Valid @RequestBody InfoInternet newObj) {
        newObj.setId(id);
        this.infoInternetService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInfoInternetById(@PathVariable("id") Long id) {
        this.infoInternetService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
