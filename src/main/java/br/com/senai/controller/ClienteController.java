package br.com.senai.controller;

import br.com.senai.dao.ClienteDAO;
import br.com.senai.model.Cliente;
import br.com.senai.service.CepNaoEncontradoException;
import br.com.senai.service.CepService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

// CONTROLLER DA CLASSE CLIENTES
// O FLUXO AQUI É SEGUIDO EM DUAS ETAPAS:
// 1) "buscarCep" MONTA UM CLIENTE COM NOME/CPF/CEP E CONSULTA O ENDEREÇO NA API
// 2) "salvarCliente" APENAS PEGA OS DADOS COMPLETOS E SALVA NO BANCO
public class ClienteController {

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoCpf;

    @FXML
    private TextField campoCep;

    @FXML
    private TextField campoLogradouro;

    @FXML
    private TextField campoBairro;

    @FXML
    private TextField campoCidade;

    @FXML
    private TextField campoUf;

    @FXML
    private Label labelMensagem;

    @FXML
    private TableView<Cliente> tabelaClientes;

    @FXML
    private TableColumn<Cliente, String> colunaNome;

    @FXML
    private TableColumn<Cliente, String> colunaCpf;

    @FXML
    private TableColumn<Cliente, String> colunaCidade;

    @FXML
    private TableColumn<Cliente, String> colunaUf;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final CepService cepService = new CepService();
    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    // GUARDA O "Cliente" EM CONSTRUÇÃO ENTRE A BUSCA DO CEP E O ATO DE SALVAR
    // FICA NULL ATÉ A BUSCA PELO CEP RETORNAR CORRETAMENTE
    private Cliente clienteEmEdicao;

    @FXML
    public void initialize() {
        configurarColunas();
        tabelaClientes.setItems(listaClientes);
        carregarClientes();
    }

    private void configurarColunas() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colunaCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colunaUf.setCellValueFactory(new PropertyValueFactory<>("uf"));
    }

    private void carregarClientes() {
        listaClientes.setAll(clienteDAO.listarTodos());
    }

    @FXML
    private void buscarCep() {
        labelMensagem.setText("");
        limparCamposEndereco();
        clienteEmEdicao = null;

        String nome = campoNome.getText();
        String cpf = campoCpf.getText();
        String cep = campoCep.getText();

        try {
            Cliente cliente = new Cliente(nome, cpf, cep);

            cepService.preencherEndereco(cliente);

            campoLogradouro.setText(cliente.getLogradouro());
            campoBairro.setText(cliente.getBairro());
            campoCidade.setText(cliente.getCidade());
            campoUf.setText(cliente.getUf());

            clienteEmEdicao = cliente;
            labelMensagem.setText("Endereço encontrado! Confira e clique em Salvar.");

        } catch (IllegalArgumentException e) {
            labelMensagem.setText(e.getMessage());
        } catch (CepNaoEncontradoException e) {
            labelMensagem.setText("CEP não encontrado. Confira o valor digitado.");
        } catch (RuntimeException e) {
            labelMensagem.setText("Erro ao consultar o CEP. Verifique sua conexão e tente novamente.");
        }
    }

    @FXML
    private void salvarCliente() {
        if (clienteEmEdicao == null) {
            labelMensagem.setText("BUSQUE O CEP ANTES DE SALVAR");
            return;
        }

        clienteDAO.salvar(clienteEmEdicao);
        labelMensagem.setText("CLIENTE SALVO COM SUCESSO");
        limparFormulario();
        carregarClientes();
    }

    private void limparCamposEndereco() {
        campoLogradouro.clear();
        campoBairro.clear();
        campoCidade.clear();
        campoUf.clear();
    }

    private void limparFormulario() {
        campoNome.clear();
        campoCpf.clear();
        campoCep.clear();
        limparCamposEndereco();
        clienteEmEdicao = null;
    }
}