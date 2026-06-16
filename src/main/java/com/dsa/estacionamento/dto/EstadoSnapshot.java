package com.dsa.estacionamento.dto;

import com.dsa.estacionamento.model.Vaga;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Estado completo do estacionamento, enviado via WebSocket (/topic/estado)
 * a cada mutação. Espelha o que os endpoints REST devolvem separadamente.
 */
@Data
@AllArgsConstructor
public class EstadoSnapshot {
    private List<Vaga> vagas;
    private List<FilaItemDTO> fila;
    private StatsResponse stats;
    private BstNodeDTO tree;
}
