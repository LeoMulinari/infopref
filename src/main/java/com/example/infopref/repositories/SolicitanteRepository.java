package com.example.infopref.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Solicitante;

@Repository
public interface SolicitanteRepository extends JpaRepository<Solicitante, Long> {
    List<Solicitante> findAllByDepartamento_Id(Long cod_dep);
}
