package com.dsa.estacionamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntradaResponse {
    private boolean sucesso;
    private String tipo; // ESTACIONADO | FILA | ERRO
    private String mensagem;
    private String placa;
    private Integer vagaNumero;
    private Integer prioridade;
    private String prioridadeLabel;

    public static EntradaResponse estacionado(String placa, int vagaNumero, int prioridade) {
        return new EntradaResponse(
                true,
                "ESTACIONADO",
                "Veículo " + placa + " estacionado na vaga " + vagaNumero + ".",
                placa,
                vagaNumero,
                prioridade,
                PrioridadeLabel.of(prioridade)
        );
    }

    public static EntradaResponse fila(String placa, int prioridade) {
        return new EntradaResponse(
                true,
                "FILA",
                "Estacionamento lotado. Veículo " + placa + " entrou na fila de espera.",
                placa,
                null,
                prioridade,
                PrioridadeLabel.of(prioridade)
        );
    }

    public static EntradaResponse erro(String placa, String mensagem) {
        return new EntradaResponse(false, "ERRO", mensagem, placa, null, null, null);
    }
}
