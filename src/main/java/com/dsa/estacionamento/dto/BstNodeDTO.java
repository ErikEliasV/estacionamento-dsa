package com.dsa.estacionamento.dto;

import com.dsa.estacionamento.dsa.bst.BSTNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BstNodeDTO {
    private int numero;
    private boolean ocupada;
    private String placaVeiculo;
    private BstNodeDTO left;
    private BstNodeDTO right;

    public static BstNodeDTO from(BSTNode node) {
        if (node == null) return null;
        return new BstNodeDTO(
                node.getVaga().getNumero(),
                node.getVaga().isOcupada(),
                node.getVaga().getPlacaVeiculo(),
                from(node.getLeft()),
                from(node.getRight())
        );
    }
}
