package com.example.infopref.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.InfoInternet;

@Repository
public interface InfoInternetRepository extends JpaRepository<InfoInternet, Long> {
    List<InfoInternet> findAllByDepartamento_Id(Long cod_dep);
}
