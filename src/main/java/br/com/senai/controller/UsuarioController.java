package br.com.senai.controller;

import br.com.senai.dao.UsuarioDAO;
import br.com.senai.model.Perfil;
import br.com.senai.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller da tela de Usuários — exclusiva do Admin (o botão que leva
 * até aqui já fica escondido para Vendedores no PrincipalController).
 */
public class UsuarioController {

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private ComboBox<Perfil> comboPerfil;

    @FXML
    private Label labelMensagem;

    @FXML
    private TableView<Usuario> tabelaUsuarios;

    @FXML
    private TableColumn<Usuario, String> colunaNome;

    @FXML
    private TableColumn<Usuario, String> colunaEmail;

    @FXML
    private TableColumn<Usuario, Perfil> colunaPerfil;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarComboPerfil();
        configurarColunas();
        tabelaUsuarios.setItems(listaUsuarios);
        carregarUsuarios();
    }

    /**
     * Preenche o ComboBox com os dois valores do enum Perfil.
     * Perfil.values() devolve um array com todos os valores do enum,
     * na ordem em que foram declarados (ADMIN, VENDEDOR).
     */
    private void configurarComboPerfil() {
        comboPerfil.setItems(FXCollections.observableArrayList(Perfil.values()));
    }

    private void configurarColunas() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colunaPerfil.setCellValueFactory(new PropertyValueFactory<>("perfil"));
    }

    private void carregarUsuarios() {
        listaUsuarios.setAll(usuarioDAO.listarTodos());
    }

    @FXML
    private void salvarUsuario() {
        labelMensagem.setText("");

        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String senha = campoSenha.getText();
        Perfil perfil = comboPerfil.getValue(); // null se nada foi selecionado

        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            labelMensagem.setText("Preencha Nome, E-mail e Senha.");
            return;
        }

        if (perfil == null) {
            labelMensagem.setText("Selecione um Perfil (Admin ou Vendedor).");
            return;
        }

        try {
            // O construtor de Usuario já valida o e-mail e transforma
            // a senha em hash BCrypt automaticamente — reaproveitamos
            // toda essa lógica da classe de domínio, sem duplicar nada aqui.
            Usuario novoUsuario = new Usuario(nome, email, senha, perfil);
            usuarioDAO.salvar(novoUsuario);

            labelMensagem.setText("Usuário salvo com sucesso!");
            limparFormulario();
            carregarUsuarios();

        } catch (IllegalArgumentException e) {
            // Pega, por exemplo, "senha menor que 6 caracteres" ou
            // "e-mail inválido", lançados pela própria classe Usuario.
            labelMensagem.setText(e.getMessage());
        } catch (RuntimeException e) {
            // Erro do banco — por exemplo, e-mail duplicado (UNIQUE),
            // já que o UsuarioDAO deixa a exceção do MySQL borbulhar.
            labelMensagem.setText("Erro ao salvar: e-mail já pode estar cadastrado.");
        }
    }

    private void limparFormulario() {
        campoNome.clear();
        campoEmail.clear();
        campoSenha.clear();
        comboPerfil.setValue(null);
    }
}