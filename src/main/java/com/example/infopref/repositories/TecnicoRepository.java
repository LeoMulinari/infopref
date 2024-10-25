package com.example.infopref.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Tecnico;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

    Optional<Tecnico> findByUserId(Long userId);
}
