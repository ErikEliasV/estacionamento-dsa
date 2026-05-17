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

    /**
     * Saída de um veículo por placa.
     * Libera a vaga na BST. Se a fila (Min-Heap) não estiver vazia, 
     * o extractMin() pega a maior prioridade e aloca na vaga recém liberada.
     */
    public String saidaVeiculo(String placa) {
        // Encontra a vaga ocupada por este veículo na BST
        // (Busca linear simples na lista in-order, mas poderia ser otimizada com um Hash auxiliar, 
        // ou fazendo uma varredura na BST)
        java.util.List<Vaga> vagas = vagasBST.listarTodas();
        Vaga vagaOcupada = null;
        for (Vaga v : vagas) {
            if (v.isOcupada() && placa.equals(v.getPlacaVeiculo())) {
                vagaOcupada = v;
                break;
            }
        }
        
        if (vagaOcupada == null) {
            return "Veículo não encontrado no estacionamento.";
        }
        
        // Libera a vaga
        vagaOcupada.setOcupada(false);
        vagaOcupada.setPlacaVeiculo(null);
        String msg = "Veículo " + placa + " saiu. Vaga " + vagaOcupada.getNumero() + " liberada.";
        
        // Verifica se há fila de espera
        if (!filaEsperaHeap.isEmpty()) {
            HeapNode proximo = filaEsperaHeap.extractMin();
            vagaOcupada.setOcupada(true);
            vagaOcupada.setPlacaVeiculo(proximo.getVeiculo().getPlaca());
            msg += "\n-> Veículo da fila " + proximo.getVeiculo().getPlaca() + 
                   " (Prioridade " + proximo.getVeiculo().getPrioridade() + ") alocado na vaga " + vagaOcupada.getNumero() + ".";
        }
        
        return msg;
    }

    /**
     * Retorna a lista de todas as vagas (In-Order).
     */
    public java.util.List<Vaga> listarVagas() {
        return vagasBST.listarTodas();
    }
}
