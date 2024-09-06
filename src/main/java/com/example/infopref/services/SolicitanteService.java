package com.example.infopref.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Solicitante;
import com.example.infopref.repositories.SolicitanteRepository;

@Service
public class SolicitanteService {
    @Autowired
    SolicitanteRepository solicitanteRepository;

    @Autowired
    DepartamentoService departamentoService;

    public Solicitante findById(Long id) {
        Optional<Solicitante> obj = this.solicitanteRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Solicitante não encontrado {id:" + id + "}");
    }

    public List<Solicitante> findAllByCod_dep(Long cod_dep) {
        this.departamentoService.findById(cod_dep);
        List<Solicitante> listDep = this.solicitanteRepository.findAllByDepartamento_Id(cod_dep);

        return listDep;
    }

    public Solicitante create(Solicitante obj) {
        obj.setId(null);

        return this.solicitanteRepository.save(obj);
    }

    public Solicitante update(Solicitante newObj) {
        Solicitante obj = this.findById(newObj.getId());

        obj.setNome(newObj.getNome());
        obj.setFone(newObj.getFone());
        obj.setId_acesso_remoto(newObj.getId_acesso_remoto());

        return this.solicitanteRepository.save(obj);
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            this.solicitanteRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar solicitante {id:" + id + "}", e);
        }
    }
}
