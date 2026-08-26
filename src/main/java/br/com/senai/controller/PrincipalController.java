package br.com.senai.controller;

import br.com.senai.app.NavegadorApp;
import br.com.senai.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PrincipalController {

    @FXML
    private Button botaoProdutos;

    @FXML
    private Button botaoClientes;  

    @FXML
    private Button botaoVendas;

    @FXML
    private Button botaoUsuarios;

    @FXML
    private Button botaoSair;

    @FXML
    private AnchorPane areaConteudo;

    @FXML
    public void initialize() {
        aplicarControleDeAcessoPorPerfil();
    }

    private void aplicarControleDeAcessoPorPerfil() {
        Usuario usuarioLogado = NavegadorApp.getUsuarioLogado();
        boolean ehAdmin = usuarioLogado != null && usuarioLogado.isAdmin();
        botaoUsuarios.setVisible(ehAdmin);
        botaoUsuarios.setManaged(ehAdmin);
    }

    private void carregarConteudo(String caminhoFxml) {
        try {
            Parent conteudo = FXMLLoader.load(getClass().getResource(caminhoFxml));

            AnchorPane.setTopAnchor(conteudo, 0.0);
            AnchorPane.setBottomAnchor(conteudo, 0.0);
            AnchorPane.setLeftAnchor(conteudo, 0.0);
            AnchorPane.setRightAnchor(conteudo, 0.0);

            areaConteudo.getChildren().setAll(conteudo);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar conteúdo: " + caminhoFxml, e);
        }
    }

    @FXML
    private void abrirProdutos() {
        carregarConteudo("/fxml/produtos.fxml");
    }

    @FXML
    private void abrirClientes() {
        carregarConteudo("/fxml/clientes.fxml");
    }

    @FXML
    private void abrirVendas() {
        carregarConteudo("/fxml/vendas.fxml");
    }

    @FXML
    private void abrirUsuarios() {
        carregarConteudo("/fxml/usuarios.fxml");
    }

    @FXML
    private void sair() {
        NavegadorApp.setUsuarioLogado(null);
        NavegadorApp.trocarTela("/fxml/login.fxml", "Easy.PDV - Login");
    }
}