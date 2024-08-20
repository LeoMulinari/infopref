package com.example.infopref.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.InfoInternet;

@Repository
public interface InfoInternetRepository extends CrudRepository<InfoInternet, Long> {
    List<InfoInternet> findAllByDepartamento_Id(Long cod_dep);
}
