package com.dsa.estacionamento.dto;

import com.dsa.estacionamento.dsa.heap.HeapNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FilaItemDTO {
    private int posicao;
    private String placa;
    private int prioridade;
    private String prioridadeLabel;
    private LocalDateTime horarioChegada;

    public static FilaItemDTO from(HeapNode node, int posicao) {
        return new FilaItemDTO(
                posicao,
                node.getVeiculo().getPlaca(),
                node.getVeiculo().getPrioridade(),
                PrioridadeLabel.of(node.getVeiculo().getPrioridade()),
                node.getVeiculo().getHorarioChegada()
        );
    }
}
