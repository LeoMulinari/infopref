package com.example.infopref.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.infopref.models.OrdemServico;
import com.example.infopref.models.DTO.OrdemServicoDTO;
import com.example.infopref.models.Enums.Prioridade;
import com.example.infopref.models.Enums.StatusOS;
import com.example.infopref.services.OrdemServicoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/osmenu")
@Validated
public class OrdemServicoController {
    @Autowired
    OrdemServicoService ordemServicoService;

    @GetMapping
    public ResponseEntity<List<OrdemServico>> getOrdemServico() {
        return ResponseEntity.ok().body(ordemServicoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServico> getOrdemServicoById(@PathVariable("id") Long id) {
        OrdemServico obj = this.ordemServicoService.findById(id);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/tecnico/{cod_tec}")
    public ResponseEntity<List<OrdemServico>> getAllOrdemServicoByCod_tec(@PathVariable("cod_tec") Long cod_tec) {
        List<OrdemServico> obj = this.ordemServicoService.findAllByCod_tec(cod_tec);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/solicitante/{cod_sol}")
    public ResponseEntity<List<OrdemServico>> getAllOrdemServicoByCod_sol(@PathVariable("cod_sol") Long cod_sol) {
        List<OrdemServico> obj = this.ordemServicoService.findAllByCod_sol(cod_sol);

        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable("status") StatusOS status) {
        Long count = ordemServicoService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/urgent-priority")
    public ResponseEntity<Long> countByUrgentPriority() {
        Long count = ordemServicoService.countByPriority(Prioridade.Urgente);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/finalized-this-month")
    public ResponseEntity<Long> countFinalizedThisMonth() {
        Long count = ordemServicoService.countFinalizedThisMonth();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/urgent-not-finalized")
    public ResponseEntity<Long> countUrgentNotFinalized() {
        Long count = ordemServicoService.countUrgentPriorityNotFinalized();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    public ResponseEntity<OrdemServico> postOrdemServico(@RequestBody OrdemServicoDTO ordemServicoDTO) {
        OrdemServico ordemServico = ordemServicoService.create(ordemServicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemServico);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> putOrdemServico(@PathVariable("id") Long id,
            @Valid @RequestBody OrdemServicoDTO newObj) {
        newObj.setId(id);
        this.ordemServicoService.update(newObj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrdemServicoById(@PathVariable("id") Long id) {
        this.ordemServicoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
