package com.example.infopref.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Departamento;
import com.example.infopref.models.Solicitante;
import com.example.infopref.repositories.SolicitanteRepository;

@Service
public class SolicitanteService {
    @Autowired
    SolicitanteRepository solicitanteRepository;

    @Autowired
    DepartamentoService departamentoService;

    @Autowired
    UserService userService;

    public List<Solicitante> findAll() {
        return solicitanteRepository.findAll();
    }

    public Solicitante findByUserId(Long userId) {
        return solicitanteRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Solicitante não encontrado para o User ID: " + userId));
    }

    public Solicitante findById(Long id) {
        userService.VerificaADMeTec();
        Optional<Solicitante> obj = this.solicitanteRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Solicitante não encontrado {id:" + id + "}");
    }

    public List<Solicitante> findAllByCod_dep(Long cod_dep) {
        departamentoService.findById(cod_dep); // Certifique-se que o departamento existe
        return solicitanteRepository.findAllByDepartamento_Id(cod_dep);
    }

    public Solicitante create(Solicitante obj) {
        userService.VerificaADMeTec();
        obj.setId(null);

        // Buscando o departamento pelo ID para garantir que a secretaria seja associada
        // automaticamente
        Departamento departamento = departamentoService.findById(obj.getDepartamento().getId());
        obj.setDepartamento(departamento); // Associa o departamento ao solicitante

        return solicitanteRepository.save(obj);
    }

    public Solicitante update(Solicitante newObj) {
        Solicitante obj = this.findById(newObj.getId());

        obj.setNome(newObj.getNome());
        obj.setFone(newObj.getFone());
        obj.setId_acesso_remoto(newObj.getId_acesso_remoto());

        // Atualizando o departamento e a secretaria
        Departamento departamento = departamentoService.findById(newObj.getDepartamento().getId());
        obj.setDepartamento(departamento); // Associando departamento atualizado

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
