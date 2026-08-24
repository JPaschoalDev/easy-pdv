package br.com.senai.model;

// CLASSE ITEMVENDA
// REPRESENTA UM ITEM DE UMA VENDA, COM O PRODUTO, A QUANTIDADE E O PREÇO UNITÁRIO NO MOMENTO DA VENDA
// AQUI É GUARDADO O PREÇO DO PRODUTO NO MOMENTO DA VENDA, PARA QUE SE O PREÇO DO PRODUTO MUDAR DEPOIS, O HISTÓRICO DE VENDAS ANTIGAS NÃO SEJA AFETADO
public class ItemVenda {

    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemVenda(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto do item de venda não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        this.produto = produto;
        this.quantidade = quantidade;
        // GUARDAMOS O PREÇO DO PRODUTO NO MOMENTO DA VENDA, PARA QUE SE O PREÇO DO PRODUTO MUDAR DEPOIS, O HISTÓRICO DE VENDAS ANTIGAS NÃO SEJA AFETADO
        this.precoUnitario = produto.getPreco();
    }

    public Produto getProduto() {
        return produto;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public double getPrecoUnitario() {
        return precoUnitario;
    }

    // CALCULA O SUBTOTAL DO ITEM (PREÇO UNITÁRIO X QUANTIDADE)
    public double getSubtotal() {
        return precoUnitario * quantidade;
    }

    @Override
    public String toString() {
        return "ItemVenda{" +
                "produto=" + produto.getNome() +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}