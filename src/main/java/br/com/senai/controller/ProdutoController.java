package br.com.senai.controller;

import br.com.senai.dao.ProdutoDAO;
import br.com.senai.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

// CONTROLLER DA TELA DE PRODUTOS
// FORMULÁRIO DE CADASTRO E LISTAGENS DOS PRODUTOS
public class ProdutoController {

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoDescricao;

    @FXML
    private TextField campoPreco;

    @FXML
    private TextField campoEstoque;

    @FXML
    private Label labelMensagem;

    @FXML
    private TableView<Produto> tabelaProdutos;

    @FXML
    private TableColumn<Produto, String> colunaNome;

    @FXML
    private TableColumn<Produto, Double> colunaPreco;

    @FXML
    private TableColumn<Produto, Integer> colunaEstoque;

    @FXML
    private TableColumn<Produto, String> colunaDescricao;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    // "ObservableList" = LISTA INTELIGENTE QUE ATUALIZA AUTOMATICAMENTE APÓS QUALQUER MUDANÇA
    private final ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColunas();
        tabelaProdutos.setItems(listaProdutos);
        carregarProdutos();
    }

    // AQUI É ORIENTADO QUAL COLUNA VAI TER CADA INFORMAÇÃO REFERENTE A PROPRIEDADE DO OBJETO PRODUTO
    private void configurarColunas() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));
        //colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    }

    // BUSCA TODOS OS PRODUTOS DO BANCO E ATUALIZA A LISTA OBSERVÁVEL
    private void carregarProdutos() {
        listaProdutos.setAll(produtoDAO.listarTodos());
    }

    @FXML
    private void salvarProduto() {
        labelMensagem.setText("");

        String nome = campoNome.getText();
        String descricao = campoDescricao.getText();
        String textoPreco = campoPreco.getText();
        String textoEstoque = campoEstoque.getText();

        if (nome.isBlank() || textoPreco.isBlank() || textoEstoque.isBlank()) {
            labelMensagem.setText("Preencha ao menos Nome, Preço e Estoque.");
            return;
        }

        double preco;
        int estoque;
        try {
            // ACEITO TANTO VÍRGULA (,) QUANTO PONTO (.)
            preco = Double.parseDouble(textoPreco.replace(",", "."));
            estoque = Integer.parseInt(textoEstoque);
        } catch (NumberFormatException e) {
            labelMensagem.setText("PREÇO DEVE SER UM NÚMERO VÁLIDO (EX.: 10.50) E ESTOQUE UM NÚMERO INTEIRO");
            return;
        }

        try {
            // CASO O USUÁRIO DIGITAR UM VALOR INVÁLIDO O CONSTRUTOR "Produto" E O "setQuantidadeEstoque"
            // A REGRA DE NEGÓCIO É VALIDADA ANTES DE SALVAR
            Produto novoProduto = new Produto(nome, descricao, preco, estoque);
            produtoDAO.salvar(novoProduto);

            labelMensagem.setText("PRODUTO SALVO COM SUCESSO");
            limparFormulario();
            carregarProdutos();
        } catch (IllegalArgumentException e) {
            labelMensagem.setText(e.getMessage());
        }
    }

    private void limparFormulario() {
        campoNome.clear();
        campoDescricao.clear();
        campoPreco.clear();
        campoEstoque.clear();
    }
}