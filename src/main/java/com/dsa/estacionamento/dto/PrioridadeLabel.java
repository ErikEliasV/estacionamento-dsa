package com.dsa.estacionamento.dto;

public final class PrioridadeLabel {
    private PrioridadeLabel() {}

    public static String of(int prioridade) {
        switch (prioridade) {
            case 1: return "PCD";
            case 2: return "Idoso";
            case 3: return "Gestante";
            case 4: return "Comum";
            default: return "Desconhecida";
        }
    }
}
