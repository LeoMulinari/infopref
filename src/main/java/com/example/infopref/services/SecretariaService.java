package com.example.infopref.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Secretaria;
import com.example.infopref.repositories.SecretariaRepository;

@Service
public class SecretariaService {
    @Autowired
    SecretariaRepository secretariaRepository;

    @Autowired
    UserService userService;

    public List<Secretaria> findAll() {
        return secretariaRepository.findAll();
    }

    public Secretaria findById(Long id) {
        Optional<Secretaria> obj = this.secretariaRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Secretaria não encontrada {id:" + id + "}");
    }

    public Secretaria create(Secretaria obj) {
        userService.VerificaADMeTec();
        obj.setId(null);

        return this.secretariaRepository.save(obj);
    }

    public Secretaria update(Secretaria newObj) {
        Secretaria obj = this.findById(newObj.getId());

        obj.setNome(newObj.getNome());
        obj.setFone(newObj.getFone());

        return this.secretariaRepository.save(obj);
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            this.secretariaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar secretaria {id:" + id + "}", e);
        }
    }
}
