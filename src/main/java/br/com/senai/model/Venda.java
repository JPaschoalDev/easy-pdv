package br.com.senai.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// CLASSE VENDA
// REPRESENTA UMA VENDA, COM CLIENTE, VENDEDOR, FORMA DE PAGAMENTO, DATA E LISTA DE ITENS
// A REGRA DE NEGÓCIO "NÃO PODE VENDER SEM ESTOQUE" É APLICADA AQUI, NO MOMENTO EM QUE UM ITEM É ADICIONADO À VENDA
public class Venda {

    private int id;
    private Cliente cliente;
    private Usuario vendedor;
    private FormaPagamento formaPagamento;
    private LocalDateTime dataVenda;
    private List<ItemVenda> itens;

    public Venda(Cliente cliente, Usuario vendedor, FormaPagamento formaPagamento) {
        if (cliente == null) {
            throw new IllegalArgumentException("Venda precisa de um Cliente.");
        }
        if (vendedor == null) {
            throw new IllegalArgumentException("Venda precisa de um vendedor (usuário logado).");
        }
        if (formaPagamento == null) {
            throw new IllegalArgumentException("Venda precisa de uma forma de pagamento.");
        }
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.formaPagamento = formaPagamento;
        this.dataVenda = LocalDateTime.now();
        this.itens = new ArrayList<>();
    }

    // ADICIONA UM PRODUTO AO CARRINHO DA VENDA, VERIFICANDO SE HÁ ESTOQUE SUFICIENTE
    // ANTES DE ACEITAR O ITEM, VERIFICAMOS SE O PRODUTO TEM ESTOQUE SUFICIENTE. SE NÃO TIVER, RETORNAMOS FALSE
    // O ESTOQUE SÓ É BAIXADO (produto.baixarEstoque) QUANDO O ITEM É EFETIVAMENTE ACEITO NA VENDA
    public boolean adicionarItem(Produto produto, int quantidade) {
        if (!produto.podeSerVendido()) {
            return false; // estoque zerado, nem tenta
        }
        boolean baixou = produto.baixarEstoque(quantidade);
        if (!baixou) {
            return false; // não havia quantidade suficiente em estoque
        }
        this.itens.add(new ItemVenda(produto, quantidade));
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    // RETORNA UMA LISTA IMUTÁVEL DE ITENS, PARA QUE NINGUÉM POSSA ADICIONAR ITENS DIRETO NA LISTA, PULANDO A VALIDAÇÃO DE ESTOQUE FEITA EM adicionarItem()
    public List<ItemVenda> getItens() {
        return List.copyOf(itens);
    }

    // CALCULA O VALOR TOTAL DA VENDA SOMANDO O SUBTOTAL DE CADA ITEM
    public double getValorTotal() {
        double total = 0;
        for (ItemVenda item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", Cliente=" + cliente.getNome() +
                ", vendedor=" + vendedor.getNome() +
                ", formaPagamento=" + formaPagamento +
                ", valorTotal=" + getValorTotal() +
                ", qtdItens=" + itens.size() +
                '}';
    }
}