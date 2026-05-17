package com.dsa.estacionamento.dsa.heap;

import com.dsa.estacionamento.model.Veiculo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HeapNode implements Comparable<HeapNode> {
    private Veiculo veiculo;

    @Override
    public int compareTo(HeapNode other) {
        // Menor número de prioridade tem precedência (1 é maior prioridade que 4)
        if (this.veiculo.getPrioridade() != other.veiculo.getPrioridade()) {
            return Integer.compare(this.veiculo.getPrioridade(), other.veiculo.getPrioridade());
        }
        // Desempate por horário de chegada (o mais antigo tem precedência)
        return this.veiculo.getHorarioChegada().compareTo(other.veiculo.getHorarioChegada());
    }
}
