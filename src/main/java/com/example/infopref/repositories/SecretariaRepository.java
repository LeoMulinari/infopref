package com.example.infopref.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Secretaria;

@Repository
public interface SecretariaRepository extends CrudRepository<Secretaria, Long> {

}
