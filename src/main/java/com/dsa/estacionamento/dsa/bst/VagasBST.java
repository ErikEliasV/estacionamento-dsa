package com.dsa.estacionamento.dsa.bst;

import com.dsa.estacionamento.model.Vaga;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VagasBST {

    @Getter
    private BSTNode root;

    public VagasBST() {
        this.root = null;
    }

    /**
     * Insere uma nova vaga na Árvore Binária de Busca.
     * Complexidade de tempo: O(log n) no caso médio.
     *
     * @param vaga Vaga a ser inserida
     */
    public void insert(Vaga vaga) {
        root = insertRec(root, vaga);
    }

    private BSTNode insertRec(BSTNode root, Vaga vaga) {
        // Se a árvore estiver vazia, retorna um novo nó
        if (root == null) {
            root = new BSTNode(vaga);
            return root;
        }

        // Caso contrário, desce pela árvore
        if (vaga.getNumero() < root.getVaga().getNumero()) {
            root.setLeft(insertRec(root.getLeft(), vaga));
        } else if (vaga.getNumero() > root.getVaga().getNumero()) {
            root.setRight(insertRec(root.getRight(), vaga));
        }

        // Retorna o ponteiro do nó (inalterado)
        return root;
    }

    /**
     * Busca uma vaga pelo número na BST.
     * Complexidade de tempo: O(log n) no caso médio.
     *
     * @param numero Número da vaga a ser buscada
     * @return A vaga se encontrada, ou null caso contrário
     */
    public Vaga search(int numero) {
        BSTNode node = searchRec(root, numero);
        return node != null ? node.getVaga() : null;
    }

    private BSTNode searchRec(BSTNode root, int numero) {
        // Casos base: raiz é nula ou o número está na raiz
        if (root == null || root.getVaga().getNumero() == numero) {
            return root;
        }

        // O número é menor que a raiz
        if (root.getVaga().getNumero() > numero) {
            return searchRec(root.getLeft(), numero);
        }

        // O número é maior que a raiz
        return searchRec(root.getRight(), numero);
    }

    /**
     * Remove uma vaga da árvore, caso precise ser desativada.
     * Cobre os 3 casos de remoção: nó folha, nó com 1 filho e nó com 2 filhos.
     *
     * @param numero Número da vaga a ser removida
     */
    public void delete(int numero) {
        root = deleteRec(root, numero);
    }

    private BSTNode deleteRec(BSTNode root, int numero) {
        // Árvore vazia
        if (root == null) {
            return root;
        }

        // Desce na árvore para encontrar o nó
        if (numero < root.getVaga().getNumero()) {
            root.setLeft(deleteRec(root.getLeft(), numero));
        } else if (numero > root.getVaga().getNumero()) {
            root.setRight(deleteRec(root.getRight(), numero));
        } else {
            // O nó foi encontrado. Agora aplicamos as regras de remoção.

            // Caso 1 e 2: Nó com apenas um filho ou sem filhos (nó folha)
            if (root.getLeft() == null) {
                return root.getRight();
            } else if (root.getRight() == null) {
                return root.getLeft();
            }

            // Caso 3: Nó com dois filhos
            // Pega o sucessor in-order (menor valor na sub-árvore direita)
            root.setVaga(minValue(root.getRight()));

            // Deleta o sucessor in-order
            root.setRight(deleteRec(root.getRight(), root.getVaga().getNumero()));
        }

        return root;
    }

    private Vaga minValue(BSTNode root) {
        Vaga minv = root.getVaga();
        while (root.getLeft() != null) {
            minv = root.getLeft().getVaga();
            root = root.getLeft();
        }
        return minv;
    }

    /**
     * Retorna a primeira vaga livre encontrada (usando in-order traversal).
     * @return Vaga livre ou null se todas estiverem ocupadas
     */
    public Vaga buscarPrimeiraVagaLivre() {
        return buscarPrimeiraVagaLivreRec(root);
    }

    private Vaga buscarPrimeiraVagaLivreRec(BSTNode root) {
        if (root == null) return null;
        
        // Busca na sub-árvore esquerda (menores números primeiro)
        Vaga vagaEsquerda = buscarPrimeiraVagaLivreRec(root.getLeft());
        if (vagaEsquerda != null) return vagaEsquerda;
        
        // Verifica a raiz
        if (!root.getVaga().isOcupada()) return root.getVaga();
        
        // Busca na sub-árvore direita
        return buscarPrimeiraVagaLivreRec(root.getRight());
    }

    /**
     * Imprime a estrutura da árvore no console para demonstração visual.
     */
    public void printTree() {
        System.out.println("----- Estrutura da BST de Vagas -----");
        printTreeRec(root, "", true);
        System.out.println("-------------------------------------");
    }

    private void printTreeRec(BSTNode node, String prefix, boolean isTail) {
        if (node != null) {
            String status = node.getVaga().isOcupada() ? "[OCUPADA]" : "[LIVRE]";
            System.out.println(prefix + (isTail ? "└── " : "├── ") + node.getVaga().getNumero() + " " + status);
            
            List<BSTNode> children = new ArrayList<>();
            if (node.getLeft() != null) children.add(node.getLeft());
            if (node.getRight() != null) children.add(node.getRight());
            
            for (int i = 0; i < children.size() - 1; i++) {
                printTreeRec(children.get(i), prefix + (isTail ? "    " : "│   "), false);
            }
            if (children.size() > 0) {
                printTreeRec(children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }

    /**
     * Retorna lista de todas as vagas in-order.
     */
    public List<Vaga> listarTodas() {
        List<Vaga> vagas = new ArrayList<>();
        listarInOrder(root, vagas);
        return vagas;
    }

    private void listarInOrder(BSTNode root, List<Vaga> vagas) {
        if (root != null) {
            listarInOrder(root.getLeft(), vagas);
            vagas.add(root.getVaga());
            listarInOrder(root.getRight(), vagas);
        }
    }
}
