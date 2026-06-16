package com.dsa.estacionamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class StatsResponse {
    private int totalVagas;
    private int vagasOcupadas;
    private int vagasLivres;
    private double taxaOcupacao;
    private int filaEspera;
    private Map<String, Integer> filaPorPrioridade;
}
