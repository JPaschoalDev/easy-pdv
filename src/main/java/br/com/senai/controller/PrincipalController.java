package br.com.senai.controller;

import br.com.senai.app.NavegadorApp;
import br.com.senai.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

// CONTROLLER DA TELA PRINCIPAL
// CADA ATRIBUITO ABAIXO MARCADO COM UM "@FXML" CORRESPONDE A UM COMPONENTE DO FXML QUE TENHA O MESMO "fx:id"
// NO SCENE BUILDER O "fx:id" PRECISA SER O MESMO QUE AQUI, CASO CONTRÁRIO A CONEXÃO FALHA
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

    // APLICA O CONTROLE DE ACESSO, ESCONDENDO ASSIM O BUTÃO "USUÁRIOS" CASO QUEM ACESSOU NÃO FOR ADMIN
    // REGRA DE NEGÓCIO = GERENCIAR USUÁRIOS É EXCLUYSIVO AO ADMIN
    private void aplicarControleDeAcessoPorPerfil() {
        Usuario usuarioLogado = NavegadorApp.getUsuarioLogado();
        boolean ehAdmin = usuarioLogado != null && usuarioLogado.isAdmin();
        botaoUsuarios.setVisible(ehAdmin);
        botaoUsuarios.setManaged(ehAdmin);
    }

    // CARREGA UM FXML DENTRO DA ÁREA DE CONTEÚDO (SEM TROCAR A TELA INTEIRA)
    // O MENU LATERAL CONTINUA VISIVEL DURANTE A NAVEGAÇÃO DO SISTEMA
    private void carregarConteudo(String caminhoFxml) {
        try {
            Parent conteudo = FXMLLoader.load(getClass().getResource(caminhoFxml));

            // FAZ O CONTEUDO OCUPAR 100% DO "AnchorPane"
            AnchorPane.setTopAnchor(conteudo, 0.0);
            AnchorPane.setBottomAnchor(conteudo, 0.0);
            AnchorPane.setLeftAnchor(conteudo, 0.0);
            AnchorPane.setRightAnchor(conteudo, 0.0);

            areaConteudo.getChildren().setAll(conteudo); // SUBSTITUI O CONTÚDO ANTERIOR PELO NOVO

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
        System.out.println("Abrir tela de Clientes (ainda vamos construir)");
    }

    @FXML
    private void abrirVendas() {
        System.out.println("Abrir tela de Vendas (ainda vamos construir)");
    }

    @FXML
    private void abrirUsuarios() {
        System.out.println("Abrir tela de Usuários (ainda vamos construir)");
    }

    @FXML
    private void sair() {
        NavegadorApp.setUsuarioLogado(null);
        NavegadorApp.trocarTela("/fxml/login.fxml", "Easy.PDV - Login");
    }
}
