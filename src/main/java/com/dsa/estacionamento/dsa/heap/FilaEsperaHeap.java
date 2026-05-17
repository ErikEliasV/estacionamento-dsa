package com.dsa.estacionamento.dsa.heap;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FilaEsperaHeap {
    
    private final List<HeapNode> heap;

    public FilaEsperaHeap() {
        this.heap = new ArrayList<>();
    }

    private int parent(int index) { return (index - 1) / 2; }
    private int leftChild(int index) { return (2 * index) + 1; }
    private int rightChild(int index) { return (2 * index) + 2; }

    /**
     * Insere um novo nó no Min-Heap e reestrutura subindo (Heapify-Up).
     * Complexidade de tempo: O(log n).
     */
    public void insert(HeapNode node) {
        heap.add(node);
        heapifyUp(heap.size() - 1);
    }

    private void heapifyUp(int index) {
        int currentIndex = index;
        while (currentIndex > 0) {
            int parentIndex = parent(currentIndex);
            // Se o nó atual for menor (mais prioritário) que o pai, troca
            if (heap.get(currentIndex).compareTo(heap.get(parentIndex)) < 0) {
                swap(currentIndex, parentIndex);
                currentIndex = parentIndex;
            } else {
                break;
            }
        }
    }

    /**
     * Remove e retorna o veículo com a maior prioridade (raiz do heap).
     * Complexidade de tempo: O(log n).
     */
    public HeapNode extractMin() {
        if (heap.isEmpty()) {
            return null;
        }

        if (heap.size() == 1) {
            return heap.remove(0);
        }

        HeapNode minNode = heap.get(0);
        // Move o último elemento para a raiz
        heap.set(0, heap.remove(heap.size() - 1));
        
        // Reestrutura descendo (Heapify-Down)
        heapifyDown(0);
        
        return minNode;
    }

    private void heapifyDown(int index) {
        int minIndex = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < heap.size() && heap.get(left).compareTo(heap.get(minIndex)) < 0) {
            minIndex = left;
        }

        if (right < heap.size() && heap.get(right).compareTo(heap.get(minIndex)) < 0) {
            minIndex = right;
        }

        if (index != minIndex) {
            swap(index, minIndex);
            heapifyDown(minIndex);
        }
    }

    private void swap(int i, int j) {
        HeapNode temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    public List<HeapNode> getHeap() {
        return new ArrayList<>(heap); // Retorna cópia rasa do array para não expor a ref direta
    }

    /**
     * Imprime a estrutura atual da Fila de Espera (Heap) no console.
     */
    public void printHeap() {
        System.out.println("----- Fila de Espera (Min-Heap) -----");
        for (int i = 0; i < heap.size(); i++) {
            String tipo = "";
            switch (heap.get(i).getVeiculo().getPrioridade()) {
                case 1: tipo = "PCD"; break;
                case 2: tipo = "Idoso"; break;
                case 3: tipo = "Gestante"; break;
                case 4: tipo = "Comum"; break;
            }
            System.out.println("Posição " + i + ": Placa " + 
                heap.get(i).getVeiculo().getPlaca() + 
                " [" + tipo + "]");
        }
        System.out.println("-------------------------------------");
    }
}
