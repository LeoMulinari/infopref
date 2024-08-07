package com.example.infopref.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Departamento;
import com.example.infopref.repositories.DepartamentoRepository;

@Service
public class DepartamentoService {
    @Autowired
    DepartamentoRepository departamentoRepository;

    @Autowired
    SecretariaService secretariaService;

    public Departamento findById(Long id) {
        Optional<Departamento> obj = this.departamentoRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Departamento não encontrado {id:" + id + "}");
    }

    public List<Departamento> findAllByCod_sec(Long cod_sec) {
        this.secretariaService.findById(cod_sec);
        List<Departamento> listDep = this.departamentoRepository.findAllBySecretaria_Id(cod_sec);

        return listDep;
    }

    public Departamento create(Departamento obj) {
        obj.setId(null);

        return this.departamentoRepository.save(obj);
    }

    public Departamento update(Departamento newObj) {
        Departamento obj = this.findById(newObj.getId());

        obj.setNome(newObj.getNome());
        obj.setFone(newObj.getFone());

        return this.departamentoRepository.save(obj);
    }

    public void deleteById(Long id) {
        try {
            this.departamentoRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar departamento {id:" + id + "}", e);
        }
    }
}
