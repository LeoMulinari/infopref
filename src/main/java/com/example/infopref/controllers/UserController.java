package com.example.infopref.controllers;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.infopref.models.User;
import com.example.infopref.models.DTO.PasswordChangeDTO;
import com.example.infopref.models.Enums.TipoUser;
import com.example.infopref.security.UserSpringSecurity;
import com.example.infopref.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserAndProfile(@PathVariable("id") Long id) {
        User obj = this.userService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<TipoUser> getUserProfile(@PathVariable("id") Long id) {
        TipoUser profile = userService.getUserProfileById(id);
        return ResponseEntity.ok().body(profile);
    }

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody @Valid User obj) {
        obj.setProfile(TipoUser.TECNICO);
        User createdUser = this.userService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).body(createdUser);
    }

    @PostMapping("/solicitante")
    public ResponseEntity<User> postUserSolicitante(@RequestBody @Valid User obj) {
        obj.setProfile(TipoUser.SOLICITANTE);
        User createdUser = this.userService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putUser(@PathVariable("id") Long id, @Valid @RequestBody User newObj) {
        newObj.setId(id);
        this.userService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable("id") Long id, @RequestBody String newPassword) {
        userService.resetPassword(id, newPassword);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        UserSpringSecurity authenticatedUser = userService.authenticated();
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.changePassword(authenticatedUser.getId(), passwordChangeDTO.getCurrentPassword(),
                passwordChangeDTO.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        this.userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
