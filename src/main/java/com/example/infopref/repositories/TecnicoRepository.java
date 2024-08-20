package com.example.infopref.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Tecnico;

@Repository
public interface TecnicoRepository extends CrudRepository<Tecnico, Long> {

}
