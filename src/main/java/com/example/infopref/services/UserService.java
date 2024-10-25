package com.example.infopref.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.infopref.models.User;
import com.example.infopref.models.Enums.TipoUser;
import com.example.infopref.repositories.UserRepository;
import com.example.infopref.security.UserSpringSecurity;
import com.example.infopref.services.exceptions.AuthorizationException;
import com.example.infopref.services.exceptions.DataBindingViolationException;
import com.example.infopref.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        // VerificaADM(); arrumar pra adm buscar todos e usuario buscar só a si mesmo

        Optional<User> obj = this.userRepository.findById(id);
        if (obj.isPresent()) {
            obj.get().getProfile();
            return obj.get();
        }
        throw new ObjectNotFoundException("Usuario não encontrado {id:" + id + "}");
    }

    public void VerificaADM() {
        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(TipoUser.ADM))
            throw new AuthorizationException("Acesso negado!");
    }

    public void VerificaADMeTec() {
        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(TipoUser.ADM) && !userSpringSecurity.hasRole(TipoUser.TECNICO))
            throw new AuthorizationException("Acesso negado!");
    }

    public User create(User obj) {
        // VerificaADM();
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        // Verificação explícita para garantir que o TipoUser está correto
        if (obj.getProfile() == TipoUser.TECNICO) {
            obj.setProfile(TipoUser.TECNICO);
        } else if (obj.getProfile() == TipoUser.ADM) {
            obj.setProfile(TipoUser.ADM);
        } else {
            obj.setProfile(TipoUser.SOLICITANTE);
        }

        System.out.println("Criando usuário com perfil: " + obj.getProfile());
        return this.userRepository.save(obj);
    }

    public User update(User newObj) {
        User obj = this.findById(newObj.getId());

        obj.setPassword(newObj.getPassword());
        obj.setPassword(this.bCryptPasswordEncoder.encode(newObj.getPassword()));

        return this.userRepository.save(obj);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = this.findById(userId);

        // Verificar se a senha atual está correta
        if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AuthorizationException("Senha atual incorreta.");
        }

        // Atualizar a senha
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public TipoUser getUserProfileById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().getProfile();
        }
        throw new ObjectNotFoundException("Usuário não encontrado {id: " + id + "}");
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException(
                    "Não é possível excluir o usuário {id:" + id + "} pois possui entidades relacionadas");
        }
    }

    public UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
