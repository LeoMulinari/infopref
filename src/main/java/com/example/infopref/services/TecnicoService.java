package com.example.infopref.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Tecnico;
import com.example.infopref.repositories.TecnicoRepository;

@Service
public class TecnicoService {
    @Autowired
    TecnicoRepository tecnicoRepository;

    @Autowired
    UserService userService;

    public Tecnico findById(Long id) {
        userService.VerificaADM();
        Optional<Tecnico> obj = this.tecnicoRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Tecnico não encontrado {id:" + id + "}");
    }

    public Tecnico create(Tecnico obj) {
        userService.VerificaADM();
        obj.setId(null);

        return this.tecnicoRepository.save(obj);
    }

    public Tecnico update(Tecnico newObj) {
        Tecnico obj = this.findById(newObj.getId());

        obj.setNome(newObj.getNome());
        obj.setFone(newObj.getFone());

        return this.tecnicoRepository.save(obj);
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            this.tecnicoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar tecnico {id:" + id + "}", e);
        }
    }
}
