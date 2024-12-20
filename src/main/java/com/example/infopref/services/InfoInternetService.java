package com.example.infopref.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.infopref.models.Departamento;
import com.example.infopref.models.InfoInternet;
import com.example.infopref.repositories.InfoInternetRepository;

@Service
public class InfoInternetService {
    @Autowired
    InfoInternetRepository infoInternetRepository;

    @Autowired
    DepartamentoService departamentoService;

    @Autowired
    UserService userService;

    public List<InfoInternet> findAll() {
        return infoInternetRepository.findAll();
    }

    public InfoInternet findById(Long id) {
        userService.VerificaADMeTec();
        Optional<InfoInternet> obj = this.infoInternetRepository.findById(id);

        if (obj.isPresent()) {
            return obj.get();
        }
        throw new RuntimeException("InfoInternet não encontrado {id:" + id + "}");
    }

    public List<InfoInternet> findAllByCod_dep(Long cod_dep) {
        this.departamentoService.findById(cod_dep);
        List<InfoInternet> listDep = this.infoInternetRepository.findAllByDepartamento_Id(cod_dep);

        return listDep;
    }

    public InfoInternet create(InfoInternet obj, Long cod_dep) {
        obj.setId(null);

        Departamento departamento = this.departamentoService.findById(cod_dep);
        obj.setDepartamento(departamento);

        return this.infoInternetRepository.save(obj);
    }

    public InfoInternet update(InfoInternet newObj) {
        InfoInternet obj = this.findById(newObj.getId());

        obj.setNome(newObj.getNome());
        obj.setSenha(newObj.getSenha());
        obj.setIp(newObj.getIp());

        return this.infoInternetRepository.save(obj);
    }

    public void deleteById(Long id) {
        findById(id);
        try {
            this.infoInternetRepository.deleteById(id);
        } catch (Exception e) {
            new RuntimeException("Erro ao deletar infoInternet {id:" + id + "}", e);
        }
    }
}
