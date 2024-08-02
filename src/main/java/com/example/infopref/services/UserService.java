package com.example.infopref.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.User;
import com.example.infopref.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;

    public User findById(UUID id){
        Optional<User> obj = this.userRepository.findById(id);

        if(obj.isPresent()){
            return obj.get();
        }
        throw new RuntimeException("Usuario não encontrado {id:"+id+"}");
    }
    
    public User create(User obj){
        obj.setId(null);

        return this.userRepository.save(obj);
    }

    public User update(User newObj){
        User obj = this.findById(newObj.getId());
        
        obj.setPassword(newObj.getPassword());

        return this.userRepository.save(obj);
    }

    public void deleteById(UUID id){
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
        }
    }
}
