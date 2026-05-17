package com.dsa.estacionamento.service;

import com.dsa.estacionamento.dsa.bst.VagasBST;
import com.dsa.estacionamento.dsa.heap.FilaEsperaHeap;
import com.dsa.estacionamento.dsa.heap.HeapNode;
import com.dsa.estacionamento.model.Vaga;
import com.dsa.estacionamento.model.Veiculo;
import org.springframework.stereotype.Service;

@Service
public class EstacionamentoService {
    
    private final VagasBST vagasBST;
    private final FilaEsperaHeap filaEsperaHeap;

    public EstacionamentoService(VagasBST vagasBST, FilaEsperaHeap filaEsperaHeap) {
        this.vagasBST = vagasBST;
        this.filaEsperaHeap = filaEsperaHeap;
    }

    /**
     * Entrada de um veículo.
     * Tenta estacionar na BST. Se estiver cheio, vai para a Fila (Min-Heap).
     */
    public String entradaVeiculo(String placa, int prioridade) {
        Veiculo veiculo = new Veiculo(placa, prioridade);
        
        // Tenta encontrar uma vaga livre na BST (O(log n) no caso médio)
        Vaga vagaLivre = vagasBST.buscarPrimeiraVagaLivre();
        
        if (vagaLivre != null) {
            // Estaciona o veículo na vaga
            vagaLivre.setOcupada(true);
            vagaLivre.setPlacaVeiculo(placa);
            return "Veículo " + placa + " estacionado na vaga " + vagaLivre.getNumero();
        } else {
            // Estacionamento lotado, vai para fila de espera (Min-Heap)
            HeapNode node = new HeapNode(veiculo);
            filaEsperaHeap.insert(node);
            return "Estacionamento lotado. Veículo " + placa + " adicionado à fila de espera com prioridade " + prioridade + ".";
        }
    }
}
