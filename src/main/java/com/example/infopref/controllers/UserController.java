package com.example.infopref.controllers;

import java.net.URI;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.example.infopref.models.User;
import com.example.infopref.models.Enums.TipoUser;
import com.example.infopref.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user") // http://localhost:8080/user
@Validated
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}") // http://localhost:8080/user/2
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User obj = this.userService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Void> postUser(@RequestBody @Valid User obj) {
        obj.setProfiles(Stream.of(TipoUser.TECNICO.getCode()).collect(Collectors.toSet()));
        this.userService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/solicitante")
    public ResponseEntity<Void> postUserSolicitante(@RequestBody @Valid User obj) {
        obj.setProfiles(Stream.of(TipoUser.SOLICITANTE.getCode()).collect(Collectors.toSet()));
        this.userService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putUser(@PathVariable("id") Long id, @Valid @RequestBody User newObj) {
        newObj.setId(id);
        this.userService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        this.userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
