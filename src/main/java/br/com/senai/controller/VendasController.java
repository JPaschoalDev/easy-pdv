package br.com.senai.controller;

import br.com.senai.app.NavegadorApp;
import br.com.senai.dao.ClienteDAO;
import br.com.senai.dao.ProdutoDAO;
import br.com.senai.dao.VendaDAO;
import br.com.senai.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

/**
 * Controller da tela de Vendas — a mais complexa do sistema, pois amarra
 * Cliente, Produto (com regra de estoque), Usuário (vendedor logado) e
 * FormaPagamento numa única operação transacional.
 *
 * O "carrinho" é montado em memória (dentro de vendaAtual) conforme o
 * usuário adiciona produtos. Nada é gravado no banco até o clique em
 * "Finalizar Venda" — é só nesse momento que VendaDAO.salvar() grava
 * tudo de uma vez, dentro de uma transação.
 */
public class VendasController {

    @FXML
    private ComboBox<Cliente> comboCliente;

    @FXML
    private ComboBox<Produto> comboProduto;

    @FXML
    private TextField campoQuantidade;

    @FXML
    private ComboBox<FormaPagamento> comboFormaPagamento;

    @FXML
    private Label labelValorTotal;

    @FXML
    private Label labelMensagem;

    @FXML
    private TableView<ItemVenda> tabelaCarrinho;

    @FXML
    private TableColumn<ItemVenda, String> colunaProdutoCarrinho;

    @FXML
    private TableColumn<ItemVenda, Integer> colunaQuantidade;

    @FXML
    private TableColumn<ItemVenda, Double> colunaPrecoUnitario;

    @FXML
    private TableColumn<ItemVenda, Double> colunaSubtotal;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final VendaDAO vendaDAO = new VendaDAO();

    private final ObservableList<ItemVenda> carrinho = FXCollections.observableArrayList();

    // A venda "em construção" — fica null até o primeiro item ser
    // adicionado ao carrinho (é nesse momento que ela é criada,
    // travando o cliente e a forma de pagamento escolhidos).
    private Venda vendaAtual;

    @FXML
    public void initialize() {
        configurarConversoresDosCombos();
        configurarColunasCarrinho();
        tabelaCarrinho.setItems(carrinho);
        recarregarListas();
        comboFormaPagamento.getSelectionModel().selectFirst();
        atualizarValorTotal();
    }

    /**
     * Por padrão, um ComboBox mostra o toString() de cada item — e o
     * toString() de Cliente/Produto que fizemos lá no início do projeto
     * é bem "cru" (Cliente{id=1, nome=...}), ruim para o usuário ler.
     * Um StringConverter diz ao ComboBox exatamente qual texto exibir
     * para cada item, sem precisar mudar o toString() da classe.
     */
    private void configurarConversoresDosCombos() {
        comboCliente.setConverter(new StringConverter<>() {
            @Override
            public String toString(Cliente cliente) {
                return cliente == null ? "" : cliente.getNome() + " (CPF: " + cliente.getCpf() + ")";
            }
            @Override
            public Cliente fromString(String string) {
                return null; // não usado: o usuário só seleciona, nunca digita
            }
        });

        comboProduto.setConverter(new StringConverter<>() {
            @Override
            public String toString(Produto produto) {
                return produto == null ? "" : produto.getNome() + " - Estoque: " + produto.getQuantidadeEstoque();
            }
            @Override
            public Produto fromString(String string) {
                return null;
            }
        });
    }

    private void configurarColunasCarrinho() {
        // colunaProdutoCarrinho não pode usar PropertyValueFactory comum,
        // porque "nome" não é um getter direto de ItemVenda — é um getter
        // de dentro do Produto que o ItemVenda carrega. Por isso usamos
        // uma expressão lambda: pegamos o ItemVenda da linha (cellData
        // .getValue()) e navegamos manualmente até getProduto().getNome().
        colunaProdutoCarrinho.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduto().getNome()));

        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaPrecoUnitario.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colunaSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    /**
     * Recarrega clientes, produtos e formas de pagamento DIRETO DO BANCO.
     * Chamado no início, e também depois de finalizar/cancelar uma venda,
     * para os ComboBox sempre refletirem o estoque real (não o estoque
     * "descontado em memória" de um carrinho anterior).
     */
    private void recarregarListas() {
        comboCliente.setItems(FXCollections.observableArrayList(clienteDAO.listarTodos()));
        comboProduto.setItems(FXCollections.observableArrayList(produtoDAO.listarTodos()));
        comboFormaPagamento.setItems(FXCollections.observableArrayList(FormaPagamento.values()));
    }

    @FXML
    private void adicionarProduto() {
        labelMensagem.setText("");

        Cliente cliente = comboCliente.getValue();
        Produto produto = comboProduto.getValue();
        FormaPagamento formaPagamento = comboFormaPagamento.getValue();
        String textoQuantidade = campoQuantidade.getText();

        if (cliente == null) {
            labelMensagem.setText("Selecione um cliente.");
            return;
        }
        if (produto == null) {
            labelMensagem.setText("Selecione um produto.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(textoQuantidade);
            if (quantidade <= 0) {
                labelMensagem.setText("Quantidade deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            labelMensagem.setText("Quantidade deve ser um número inteiro.");
            return;
        }

        // Cria a Venda em memória na hora do PRIMEIRO item adicionado —
        // a partir daqui, cliente e forma de pagamento ficam "travados"
        // para essa venda (por isso desabilitamos os ComboBox abaixo).
        if (vendaAtual == null) {
            vendaAtual = new Venda(cliente, NavegadorApp.getUsuarioLogado(), formaPagamento);
            comboCliente.setDisable(true);
            comboFormaPagamento.setDisable(true);
        }

        // Reaproveita a regra de estoque que já existe na classe Venda
        // (que por sua vez reaproveita Produto.baixarEstoque) — nenhuma
        // validação de estoque nova foi escrita aqui.
        boolean adicionado = vendaAtual.adicionarItem(produto, quantidade);

        if (!adicionado) {
            labelMensagem.setText("Estoque insuficiente para " + produto.getNome() + ".");
            return;
        }

        carrinho.setAll(vendaAtual.getItens());
        atualizarValorTotal();
        campoQuantidade.clear();
        comboProduto.getSelectionModel().clearSelection();
    }

    private void atualizarValorTotal() {
        double total = vendaAtual == null ? 0.0 : vendaAtual.getValorTotal();
        labelValorTotal.setText(String.format("Total: R$ %.2f", total));
    }

    @FXML
    private void finalizarVenda() {
        if (vendaAtual == null || vendaAtual.getItens().isEmpty()) {
            labelMensagem.setText("Adicione ao menos um produto ao carrinho antes de finalizar.");
            return;
        }

        vendaDAO.salvar(vendaAtual);

        // VendaDAO.salvar só grava a venda e os itens — a baixa de
        // estoque, até aqui, só existe nos objetos Produto EM MEMÓRIA
        // (feita dentro de Venda.adicionarItem). Por isso persistimos
        // cada produto alterado no banco agora, um por um.
        for (ItemVenda item : vendaAtual.getItens()) {
            produtoDAO.atualizar(item.getProduto());
        }

        labelMensagem.setText("Venda finalizada com sucesso! Total: R$ " + String.format("%.2f", vendaAtual.getValorTotal()));

        limparVenda();
    }

    @FXML
    private void cancelarVenda() {
        // Descarta o carrinho SEM salvar. Importante: os objetos Produto
        // usados no carrinho já tiveram o estoque reduzido em memória
        // (via baixarEstoque), então recarregarListas() é essencial aqui
        // — sem isso, o ComboBox de produtos continuaria mostrando um
        // estoque "fantasma", menor do que o valor real do banco.
        limparVenda();
        labelMensagem.setText("Venda cancelada.");
    }

    private void limparVenda() {
        vendaAtual = null;
        carrinho.clear();
        campoQuantidade.clear();
        comboCliente.setDisable(false);
        comboFormaPagamento.setDisable(false);
        comboCliente.getSelectionModel().clearSelection();
        comboProduto.getSelectionModel().clearSelection();
        recarregarListas();
        comboFormaPagamento.getSelectionModel().selectFirst();
        atualizarValorTotal();
    }
}