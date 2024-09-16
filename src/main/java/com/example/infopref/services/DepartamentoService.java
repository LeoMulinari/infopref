package com.example.infopref.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Departamento;
import com.example.infopref.models.Equipamento;
import com.example.infopref.models.Secretaria;
import com.example.infopref.models.DTO.DepartamentoDTO;
import com.example.infopref.repositories.DepartamentoRepository;
import com.example.infopref.repositories.EquipamentoRepository;
import com.example.infopref.repositories.SecretariaRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartamentoService {
    @Autowired
    DepartamentoRepository departamentoRepository;

    @Autowired
    SecretariaRepository secretariaRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

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
        // userService.VerificaADMeTec();
        Departamento departamento = new Departamento();
        departamento.setNome(dto.getNome());
        departamento.setFone(dto.getFone());
        // departamento.setSecretaria(secretariaRepository.findById(dto.getSecretariaId()).orElseThrow());

        // Buscar a secretaria existente com base no ID enviado
        Secretaria secretaria = secretariaRepository.findById(dto.getSecretariaId())
                .orElseThrow(() -> new RuntimeException("Secretaria não encontrada"));
        departamento.setSecretaria(secretaria);

        /*
         * Associando equipamentos
         * List<Equipamento> equipamentos = (List<Equipamento>) equipamentoRepository
         * .findAllById(dto.getEquipamentosIds());
         * departamento.setEquipamentos(equipamentos);
         */
        departamento.setEquipamentos(new ArrayList<>()); // Equipamentos vazios na criação
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public Departamento update(DepartamentoDTO dto) {
        Departamento departamento = departamentoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        departamento.setNome(dto.getNome());
        departamento.setFone(dto.getFone());
        // departamento.setSecretaria(secretariaRepository.findById(dto.getSecretariaId()).orElseThrow());

        List<Equipamento> equipamentos = (List<Equipamento>) equipamentoRepository
                .findAllById(dto.getEquipamentosIds());
        departamento.setEquipamentos(equipamentos);

        return departamentoRepository.save(departamento);
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            this.departamentoRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar departamento {id:" + id + "}", e);
        }
    }
}
