package com.dsa.estacionamento.controller;

import com.dsa.estacionamento.service.EstacionamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estacionamento")
public class EstacionamentoController {

    private final EstacionamentoService service;

    public EstacionamentoController(EstacionamentoService service) {
        this.service = service;
    }

    @PostMapping("/entrada")
    public ResponseEntity<String> entradaVeiculo(@RequestParam String placa, @RequestParam int prioridade) {
        String resultado = service.entradaVeiculo(placa, prioridade);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/saida")
    public ResponseEntity<String> saidaVeiculo(@RequestParam String placa) {
        String resultado = service.saidaVeiculo(placa);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/vagas")
    public ResponseEntity<java.util.List<com.dsa.estacionamento.model.Vaga>> listarVagas() {
        return ResponseEntity.ok(service.listarVagas());
    }
}
