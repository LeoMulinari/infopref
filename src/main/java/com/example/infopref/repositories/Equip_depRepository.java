package com.example.infopref.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Equip_dep;

@Repository
public interface Equip_depRepository extends JpaRepository<Equip_dep, Long> {
    List<Equip_dep> findAllByDepartamento_Id(Long cod_dep);

    Optional<Equip_dep> findByEquipamento_IdAndDepartamento_Id(Long equipamento_id, Long departamento_id);

    void deleteByEquipamento_IdAndDepartamento_Id(Long equipamento_id, Long departamento_id);
}
