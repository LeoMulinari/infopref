package com.example.infopref.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.infopref.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Transactional(readOnly = true)
    User findByUsername(String username);
}
