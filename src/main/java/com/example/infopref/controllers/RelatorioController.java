package com.example.infopref.controllers;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.infopref.services.RelatorioService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/ordens-de-servico")
    public ResponseEntity<InputStreamResource> gerarRelatorio(
            @RequestParam("dataInicio") String dataInicio,
            @RequestParam("dataFim") String dataFim,
            @RequestParam("tipo") String tipo,
            @RequestParam(value = "filtro", required = false) String filtro) {
        System.out.println("Requisição recebida para gerar relatório");
        System.out.println("Parâmetros - Data Início: " + dataInicio + ", Data Fim: " + dataFim + ", Tipo: " + tipo
                + ", Filtro: " + filtro);

        ByteArrayInputStream bis = relatorioService.gerarRelatorio(dataInicio, dataFim, tipo, filtro);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=relatorio.pdf");

        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
