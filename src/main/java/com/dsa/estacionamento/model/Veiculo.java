package com.dsa.estacionamento.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Veiculo {
    private String placa;
    private int prioridade; // 1: PCD, 2: Idoso, 3: Gestante, 4: Comum
    private LocalDateTime horarioChegada;

    public Veiculo(String placa, int prioridade) {
        this.placa = placa;
        this.prioridade = prioridade;
        this.horarioChegada = LocalDateTime.now();
    }
}
