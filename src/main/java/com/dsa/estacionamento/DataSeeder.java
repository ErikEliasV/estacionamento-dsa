package com.dsa.estacionamento;

import com.dsa.estacionamento.dsa.bst.VagasBST;
import com.dsa.estacionamento.model.Vaga;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final VagasBST vagasBST;

    public DataSeeder(VagasBST vagasBST) {
        this.vagasBST = vagasBST;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Inicializando o sistema com 10 vagas...");
        
        // Inserir vagas de forma que a BST fique razoavelmente balanceada
        int[] vagasIniciais = {5, 3, 8, 2, 4, 7, 9, 1, 6, 10};
        
        for (int numero : vagasIniciais) {
            vagasBST.insert(new Vaga(numero));
        }
        
        System.out.println("Vagas inicializadas com sucesso.");
        vagasBST.printTree();
    }
}
