package com.dsa.estacionamento.dsa.bst;

import com.dsa.estacionamento.model.Vaga;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BSTNode {
    private Vaga vaga;
    private BSTNode left;
    private BSTNode right;

    public BSTNode(Vaga vaga) {
        this.vaga = vaga;
        this.left = null;
        this.right = null;
    }
}
