package com.example.infopref.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Secretaria;

@Repository
public interface SecretariaRepository extends JpaRepository<Secretaria, Long> {

}
