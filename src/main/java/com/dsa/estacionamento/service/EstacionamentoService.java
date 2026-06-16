package com.dsa.estacionamento.service;

import com.dsa.estacionamento.dsa.bst.VagasBST;
import com.dsa.estacionamento.dsa.heap.FilaEsperaHeap;
import com.dsa.estacionamento.dsa.heap.HeapNode;
import com.dsa.estacionamento.dto.BstNodeDTO;
import com.dsa.estacionamento.dto.EntradaResponse;
import com.dsa.estacionamento.dto.EstadoSnapshot;
import com.dsa.estacionamento.dto.FilaItemDTO;
import com.dsa.estacionamento.dto.PrioridadeLabel;
import com.dsa.estacionamento.dto.SaidaResponse;
import com.dsa.estacionamento.dto.StatsResponse;
import com.dsa.estacionamento.model.Vaga;
import com.dsa.estacionamento.model.Veiculo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstacionamentoService {

    private final VagasBST vagasBST;
    private final FilaEsperaHeap filaEsperaHeap;
    private final SimpMessagingTemplate messagingTemplate;

    public EstacionamentoService(VagasBST vagasBST, FilaEsperaHeap filaEsperaHeap,
                                 SimpMessagingTemplate messagingTemplate) {
        this.vagasBST = vagasBST;
        this.filaEsperaHeap = filaEsperaHeap;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Entrada de um veículo.
     * Tenta estacionar na BST. Se estiver cheio, vai para a Fila (Min-Heap).
     */
    public EntradaResponse entradaVeiculo(String placa, int prioridade) {
        if (placa == null || placa.isBlank()) {
            return EntradaResponse.erro(placa, "Placa inválida.");
        }
        if (prioridade < 1 || prioridade > 4) {
            return EntradaResponse.erro(placa, "Prioridade inválida (use 1=PCD, 2=Idoso, 3=Gestante, 4=Comum).");
        }

        String placaNormalizada = placa.trim().toUpperCase();

        // Não permite a mesma placa duas vezes (no estacionamento ou na fila)
        if (placaJaPresente(placaNormalizada)) {
            return EntradaResponse.erro(placaNormalizada,
                    "Veículo " + placaNormalizada + " já está no estacionamento ou na fila.");
        }

        Veiculo veiculo = new Veiculo(placaNormalizada, prioridade);

        // Tenta encontrar uma vaga livre na BST (O(log n) no caso médio)
        Vaga vagaLivre = vagasBST.buscarPrimeiraVagaLivre();

        if (vagaLivre != null) {
            vagaLivre.setOcupada(true);
            vagaLivre.setPlacaVeiculo(placaNormalizada);
            publicar();
            return EntradaResponse.estacionado(placaNormalizada, vagaLivre.getNumero(), prioridade);
        }

        // Estacionamento lotado, vai para fila (Min-Heap)
        filaEsperaHeap.insert(new HeapNode(veiculo));
        publicar();
        return EntradaResponse.fila(placaNormalizada, prioridade);
    }

    /**
     * Saída de um veículo por placa.
     */
    public SaidaResponse saidaVeiculo(String placa) {
        if (placa == null || placa.isBlank()) {
            return SaidaResponse.erro("Placa inválida.");
        }
        String placaNormalizada = placa.trim().toUpperCase();

        List<Vaga> vagas = vagasBST.listarTodas();
        Vaga vagaOcupada = null;
        for (Vaga v : vagas) {
            if (v.isOcupada() && placaNormalizada.equals(v.getPlacaVeiculo())) {
                vagaOcupada = v;
                break;
            }
        }

        if (vagaOcupada == null) {
            return SaidaResponse.erro("Veículo " + placaNormalizada + " não encontrado no estacionamento.");
        }

        int vagaLiberada = vagaOcupada.getNumero();
        vagaOcupada.setOcupada(false);
        vagaOcupada.setPlacaVeiculo(null);

        String mensagem = "Veículo " + placaNormalizada + " saiu. Vaga " + vagaLiberada + " liberada.";
        SaidaResponse.VeiculoEntranteDTO entrante = null;

        if (!filaEsperaHeap.isEmpty()) {
            HeapNode proximo = filaEsperaHeap.extractMin();
            vagaOcupada.setOcupada(true);
            vagaOcupada.setPlacaVeiculo(proximo.getVeiculo().getPlaca());
            entrante = new SaidaResponse.VeiculoEntranteDTO(
                    proximo.getVeiculo().getPlaca(),
                    proximo.getVeiculo().getPrioridade(),
                    PrioridadeLabel.of(proximo.getVeiculo().getPrioridade()),
                    vagaLiberada
            );
            mensagem += " Veículo " + proximo.getVeiculo().getPlaca() + " da fila assumiu a vaga.";
        }

        publicar();
        return new SaidaResponse(true, mensagem, placaNormalizada, vagaLiberada, entrante);
    }

    public List<Vaga> listarVagas() {
        return vagasBST.listarTodas();
    }

    public List<FilaItemDTO> listarFila() {
        List<HeapNode> heap = filaEsperaHeap.getHeap();
        List<FilaItemDTO> out = new ArrayList<>(heap.size());
        for (int i = 0; i < heap.size(); i++) {
            out.add(FilaItemDTO.from(heap.get(i), i));
        }
        return out;
    }

    public StatsResponse stats() {
        List<Vaga> vagas = vagasBST.listarTodas();
        int total = vagas.size();
        int ocupadas = 0;
        for (Vaga v : vagas) {
            if (v.isOcupada()) ocupadas++;
        }
        int livres = total - ocupadas;
        double taxa = total == 0 ? 0.0 : ((double) ocupadas / total) * 100.0;

        Map<String, Integer> filaPorPrioridade = new HashMap<>();
        filaPorPrioridade.put("PCD", 0);
        filaPorPrioridade.put("Idoso", 0);
        filaPorPrioridade.put("Gestante", 0);
        filaPorPrioridade.put("Comum", 0);

        List<HeapNode> heap = filaEsperaHeap.getHeap();
        for (HeapNode n : heap) {
            String label = PrioridadeLabel.of(n.getVeiculo().getPrioridade());
            filaPorPrioridade.merge(label, 1, Integer::sum);
        }

        return new StatsResponse(total, ocupadas, livres, Math.round(taxa * 10.0) / 10.0,
                heap.size(), filaPorPrioridade);
    }

    /**
     * Reseta o estado: libera todas as vagas e limpa a fila.
     * Útil para apresentação/demonstração.
     */
    public void reset() {
        for (Vaga v : vagasBST.listarTodas()) {
            v.setOcupada(false);
            v.setPlacaVeiculo(null);
        }
        while (!filaEsperaHeap.isEmpty()) {
            filaEsperaHeap.extractMin();
        }
        publicar();
    }

    /**
     * Ocupa todas as vagas livres com veículos de demonstração.
     * Útil na apresentação para mostrar o estacionamento lotado e a fila entrando em ação.
     */
    public void lotar() {
        for (Vaga v : vagasBST.listarTodas()) {
            if (!v.isOcupada()) {
                v.setOcupada(true);
                v.setPlacaVeiculo("DEMO" + v.getNumero());
            }
        }
        publicar();
    }

    /**
     * Reconfigura o estacionamento com N vagas. Limpa estado atual.
     */
    public void configurar(int totalVagas) {
        if (totalVagas < 1 || totalVagas > 64) {
            throw new IllegalArgumentException("Quantidade de vagas deve estar entre 1 e 64.");
        }
        while (!filaEsperaHeap.isEmpty()) {
            filaEsperaHeap.extractMin();
        }
        vagasBST.rebuild(totalVagas);
        publicar();
    }

    /**
     * Monta o estado completo (vagas, fila, stats e árvore) para envio via WebSocket.
     */
    public EstadoSnapshot montarSnapshot() {
        return new EstadoSnapshot(listarVagas(), listarFila(), stats(), bstTree());
    }

    /**
     * Publica o estado atual no tópico /topic/estado para todos os clientes conectados.
     */
    private void publicar() {
        messagingTemplate.convertAndSend("/topic/estado", montarSnapshot());
    }

    /**
     * Devolve a árvore BST como estrutura aninhada (para visualização).
     */
    public BstNodeDTO bstTree() {
        return BstNodeDTO.from(vagasBST.getRoot());
    }

    private boolean placaJaPresente(String placa) {
        for (Vaga v : vagasBST.listarTodas()) {
            if (v.isOcupada() && placa.equals(v.getPlacaVeiculo())) return true;
        }
        for (HeapNode n : filaEsperaHeap.getHeap()) {
            if (placa.equals(n.getVeiculo().getPlaca())) return true;
        }
        return false;
    }
}
