package com.dsa.estacionamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaidaResponse {
    private boolean sucesso;
    private String mensagem;
    private String placaSaida;
    private Integer vagaLiberada;
    private VeiculoEntranteDTO veiculoEntrante;

    @Data
    @AllArgsConstructor
    public static class VeiculoEntranteDTO {
        private String placa;
        private int prioridade;
        private String prioridadeLabel;
        private int vagaOcupada;
    }

    public static SaidaResponse erro(String mensagem) {
        return new SaidaResponse(false, mensagem, null, null, null);
    }
}
