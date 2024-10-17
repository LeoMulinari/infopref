package com.example.infopref.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.infopref.models.Equip_dep;

@Repository
public interface Equip_depRepository extends JpaRepository<Equip_dep, Long> {
    List<Equip_dep> findAllByDepartamento_Id(Long cod_dep);

    List<Equip_dep> findByEquipamentoId(Long equipamentoId);

    Optional<Equip_dep> findByEquipamento_IdAndDepartamento_Id(Long equipamento_id, Long departamento_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Equip_dep e WHERE e.equipamento.id = :equipamentoId")
    void deleteByEquipamentoId(Long equipamentoId);

    void deleteByEquipamento_IdAndDepartamento_Id(Long equipamento_id, Long departamento_id);
}
