package com.example.infopref.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.infopref.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    
}
