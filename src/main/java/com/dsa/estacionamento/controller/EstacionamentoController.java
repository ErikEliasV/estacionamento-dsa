package com.dsa.estacionamento.controller;

import com.dsa.estacionamento.dto.BstNodeDTO;
import com.dsa.estacionamento.dto.EntradaResponse;
import com.dsa.estacionamento.dto.FilaItemDTO;
import com.dsa.estacionamento.dto.SaidaResponse;
import com.dsa.estacionamento.dto.StatsResponse;
import com.dsa.estacionamento.model.Vaga;
import com.dsa.estacionamento.service.EstacionamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estacionamento")
public class EstacionamentoController {

    private final EstacionamentoService service;

    public EstacionamentoController(EstacionamentoService service) {
        this.service = service;
    }

    @PostMapping("/entrada")
    public ResponseEntity<EntradaResponse> entradaVeiculo(@RequestBody Map<String, Object> body) {
        String placa = body.get("placa") != null ? String.valueOf(body.get("placa")) : null;
        int prioridade = body.get("prioridade") != null
                ? Integer.parseInt(String.valueOf(body.get("prioridade")))
                : 4;
        EntradaResponse resp = service.entradaVeiculo(placa, prioridade);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/saida")
    public ResponseEntity<SaidaResponse> saidaVeiculo(@RequestBody Map<String, Object> body) {
        String placa = body.get("placa") != null ? String.valueOf(body.get("placa")) : null;
        SaidaResponse resp = service.saidaVeiculo(placa);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/vagas")
    public ResponseEntity<List<Vaga>> listarVagas() {
        return ResponseEntity.ok(service.listarVagas());
    }

    @GetMapping("/fila")
    public ResponseEntity<List<FilaItemDTO>> listarFila() {
        return ResponseEntity.ok(service.listarFila());
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> stats() {
        return ResponseEntity.ok(service.stats());
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> reset() {
        service.reset();
        return ResponseEntity.ok(Map.of("sucesso", true, "mensagem", "Estado reiniciado."));
    }

    @PostMapping("/lotar")
    public ResponseEntity<Map<String, Object>> lotar() {
        service.lotar();
        return ResponseEntity.ok(Map.of("sucesso", true, "mensagem", "Todas as vagas foram ocupadas."));
    }

    @PostMapping("/configurar")
    public ResponseEntity<Map<String, Object>> configurar(@RequestBody Map<String, Object> body) {
        int total = body.get("totalVagas") != null
                ? Integer.parseInt(String.valueOf(body.get("totalVagas")))
                : 10;
        try {
            service.configurar(total);
            return ResponseEntity.ok(Map.of(
                    "sucesso", true,
                    "mensagem", "Configurado com " + total + " vagas.",
                    "totalVagas", total
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "sucesso", false,
                    "mensagem", e.getMessage()
            ));
        }
    }

    @GetMapping("/bst-tree")
    public ResponseEntity<BstNodeDTO> bstTree() {
        return ResponseEntity.ok(service.bstTree());
    }
}
