package com.example.infopref.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.Equip_os;

@Repository
public interface Equip_osRepository extends JpaRepository<Equip_os, Long> {
    List<Equip_os> findAllByOrdemServico_Id(Long cod_os);
}
