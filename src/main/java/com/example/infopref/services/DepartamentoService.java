package com.example.infopref.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Departamento;
import com.example.infopref.models.Secretaria;
import com.example.infopref.models.Solicitante;
import com.example.infopref.models.DTO.DepartamentoDTO;
import com.example.infopref.repositories.DepartamentoRepository;
import com.example.infopref.repositories.SecretariaRepository;
import com.example.infopref.repositories.SolicitanteRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartamentoService {
    @Autowired
    DepartamentoRepository departamentoRepository;

    @Autowired
    SecretariaRepository secretariaRepository;

    @Autowired
    SolicitanteRepository solicitanteRepository;

    @Autowired
    UserService userService;

    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    public Departamento findById(Long id) {
        userService.VerificaADMeTec();
        Optional<Departamento> obj = this.departamentoRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("Departamento não encontrado {id:" + id + "}");
    }

    public List<Departamento> findAllByCod_sec(Long cod_sec) {
        this.secretariaRepository.findById(cod_sec);
        List<Departamento> listDep = this.departamentoRepository.findAllBySecretaria_Id(cod_sec);

        return listDep;
    }

    @Transactional
    public Departamento create(DepartamentoDTO dto) {
        Departamento departamento = new Departamento();
        departamento.setNome(dto.getNome());
        departamento.setFone(dto.getFone());
    
        Secretaria secretaria = secretariaRepository.findById(dto.getSecretariaId())
                .orElseThrow(() -> new RuntimeException("Secretaria não encontrada"));
        departamento.setSecretaria(secretaria);

        departamento.setEquipamentos(new ArrayList<>()); 

        return departamentoRepository.save(departamento);
    }

    @Transactional
    public Departamento update(DepartamentoDTO dto) {
        Departamento departamento = departamentoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        departamento.setNome(dto.getNome());
        departamento.setFone(dto.getFone());
        return departamentoRepository.save(departamento);
    }

    public void deleteById(Long id) {
        findById(id);

        List<Solicitante> solicitantes = solicitanteRepository.findAllByDepartamento_Id(id);
        if (!solicitantes.isEmpty()) {
            throw new RuntimeException(
                    "Não é possível excluir este departamento, pois ele está associado a solicitantes.");
        }
        try {
            this.departamentoRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar departamento {id:" + id + "}", e);
        }
    }
}
