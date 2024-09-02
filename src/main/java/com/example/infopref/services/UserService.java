package com.example.infopref.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.infopref.models.User;
import com.example.infopref.models.Enums.TipoUser;
import com.example.infopref.repositories.UserRepository;
import com.example.infopref.services.exceptions.DataBindingViolationException;
import com.example.infopref.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    public User findById(Long id) {
        Optional<User> obj = this.userRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new ObjectNotFoundException("Usuario não encontrado {id:" + id + "}");
    }

    public User create(User obj) {
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        // rever para colocar o tipo solicitante para ser criado tambem
        obj.setProfiles(Stream.of(TipoUser.TECNICO.getCode()).collect(Collectors.toSet()));

        return this.userRepository.save(obj);
    }

    public User update(User newObj) {
        User obj = this.findById(newObj.getId());

        obj.setPassword(newObj.getPassword());
        obj.setPassword(this.bCryptPasswordEncoder.encode(newObj.getPassword()));

        return this.userRepository.save(obj);
    }

    public void deleteById(Long id) {
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException(
                    "Não é possível excluir o usuário {id:" + id + "} pois possui entidades relacionadas");
        }
    }
}
