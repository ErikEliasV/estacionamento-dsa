package com.dsa.estacionamento.model;

import lombok.Data;

@Data
public class Vaga {
    private int numero;
    private boolean ocupada;
    private String placaVeiculo;

    public Vaga(int numero) {
        this.numero = numero;
        this.ocupada = false;
        this.placaVeiculo = null;
    }
}
