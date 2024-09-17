package com.example.infopref.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Departamento;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    List<Departamento> findAllBySecretaria_Id(Long cod_sec);

    @Query("SELECT d FROM Departamento d LEFT JOIN d.equipamentos")
    List<Departamento> findAllWithEquipamentos();
}
