package com.example.infopref.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Equipamento;

@Repository
public interface EquipamentoRepository extends CrudRepository<Equipamento, Long> {

}
