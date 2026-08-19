package br.com.senai;

public class Main {

    public static void main(String[] args) {

        Produto produto1 = new Produto("Notebook", "Notebook Gamer", 5000.00, 10);

        System.out.println(produto1);

        produto1.baixarEstoque(15);

        System.out.println(produto1.getQuantidadeEstoque());
    }
}