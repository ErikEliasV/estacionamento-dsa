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
}
