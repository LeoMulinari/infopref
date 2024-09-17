package com.example.infopref.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Equipamento;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
    @Query("SELECT e FROM Equipamento e JOIN e.departamentos d WHERE d.id = :id")
    List<Equipamento> findByDepartamento(@Param("id") Long id);

    @Query("SELECT e FROM Equipamento e JOIN e.departamentos")
    List<Equipamento> findAllWithDepartamento();
}
