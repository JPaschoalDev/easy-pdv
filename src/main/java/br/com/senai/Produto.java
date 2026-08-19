package br.com.senai;

// CLASSE "Produto"
// REPRESENTA UM PRODUTO DO CATÁLOGO DE VENDAS E SUAS INFORMAÇÕES (NOME, DESCRIÇÃO,  PREÇO, QAUNT. ESTOQUE)
// GARANTE QUE A REGRA DO NÉGOCIO SEJA CUMPRIDA (ESTOQUE NUNCA  NEGATIVO)
public class Produto {

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidadeEstoque;

    // CONSTRUTOR USADO QUANDO O PRODUTO AINDA NÃO TEM ID (NÃO VEM DO BANCO)
    public Produto(String nome, String descricao, double preco, int quantidadeEstoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        // USAMOS O SETTER PARA GARANTIR QUE A REGRA DE NEGÓCIO SEJA CUMPRIDA (ESTOQUE NÃO PODE SER NEGATIVO)
        this.setQuantidadeEstoque(quantidadeEstoque);
    }

    // CONSTRUTOR USADO QUANDO O PRODUTO JÁ VEM DO BANCO (JÁ TEM ID)
    public Produto(int id, String nome, String descricao, double preco, int quantidadeEstoque) {
        this(nome, descricao, preco, quantidadeEstoque);
        this.id = id;
    }

    // GETTERS E SETTERS (ENCAPSULAMENTO)

    // iD
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    // NOME
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    // DESCRIÇÃO
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    // PREÇO
    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }
    // QUANTIDADE EM ESTOQUE
    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    // "setQuantidadeEstoque" = CASO UM VALOR NEGATIVO SEJA PASSADO PARA O ESTOQUE, É LANÇADO UMA EXCEÇÃO
    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException(
                    "Quantidade em estoque não pode ser negativa. Valor recebido: " + quantidadeEstoque
            );
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // "baixarEstoque"
    // RETIRA UMA QUANTIDADE DO ESTOQUE (USADO NA HORA DA VENDA)
    // RETORNA "true" SE A BAIXA FOI FEITA, "false" SE NÃO HAVIA ESTOQUE SUFICIENTE
    // ESSE METODO É O PONTO CENTRAL DA REGRA "NÃO PODE VENDER SEM ESTOQUE"
    public boolean baixarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a baixar deve ser maior que zero.");
        }
        if (quantidade > this.quantidadeEstoque) {
            // NÃO HÁ ESTOQUE SUFICIENTE PARA REALIZAR A BAIXA
            System.out.println("Não há estoque suficiente para baixar " + quantidade + " unidades do produto " + this.nome + ". Estoque atual: " + this.quantidadeEstoque);
            return false;
        }
        this.quantidadeEstoque -= quantidade;
        return true;
    }

    // "podeSerVendido" = INDICA SE O PRODUTO PODE SER VENDIDO (OU SE TEM ESTOQUE DISPONÍVEL)
    public boolean podeSerVendido() {
        return this.quantidadeEstoque > 0;
    }

    @Override
    public String toString() {
        return "PRODUTO iD ["+id+"]\n"+
                "\nNOME = " +nome+
                "\nPREÇO = R% " +preco+
                "\nQUANTIDADE EM ESTOQUE = "+quantidadeEstoque;
    }
}
