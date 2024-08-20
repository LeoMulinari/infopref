package com.example.infopref.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Departamento;

@Repository
public interface DepartamentoRepository extends CrudRepository<Departamento, Long> {
    List<Departamento> findAllBySecretaria_Id(Long cod_sec);
}
