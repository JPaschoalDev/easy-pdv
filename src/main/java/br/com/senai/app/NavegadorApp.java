package br.com.senai.app;

import br.com.senai.model.Usuario;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// CENTRALIZA A TROCA DE TELAS DO SISTEMA
// AO IVÉS DE CADA CONTROLLER SABER COMO TROCAR DE UMA TELA PARA OUTRA
// OS MÉTODOS ESTÁTICOS DESSA CLASSE SÃO CHAMADOS, ISSO TORNA O SISTEMA ESCALONÁVEL E DE FÁCIL MANUTENÇÃO
// AQUI TAMBÉM É GUARDADO O USUÁRIO ATUAL QUE ESTÁ LOGADO
public class NavegadorApp {

    private static Stage stagePrincipal;
    private static Usuario usuarioLogado;

    // METODO CHAMADO UMA ÚNICA VEZ PELO "MainApp" PARA GUARDAR A REFERÊNCIA DO STAGE PRINCIPAL
    // A PARTIR DESSE PONTO QUALQUER CONTROLLER DO SISTEMA PODE PEDIR PARA TROCAR DE TELA
    public static void inicializar(Stage stage) {
        stagePrincipal = stage;
    }

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    // METODO RESPONSÁVEL POR TROCAR A TELA ATUAL PELA TELA INDICADA
    // EXEMPLO: "NavegadorApp.trocarTela("/fxml/principal.fxml", "Sistema de Vendas - Início");"
    public static void trocarTela(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(NavegadorApp.class.getResource(fxml));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stagePrincipal.setScene(scene);
            stagePrincipal.setTitle(titulo);

            // Trata o tamanho da janela de acordo com a tela carregada
            if (fxml.contains("login")) {
                // Libera os limites para a tela de login encolher ao tamanho original
                stagePrincipal.setMinWidth(0);
                stagePrincipal.setMinHeight(0);
                stagePrincipal.setWidth(Double.NaN);
                stagePrincipal.setHeight(Double.NaN);
            } else {
                // Garante que a tela principal tenha espaço suficiente para o conteúdo interno (produtos, vendas, etc.)
                stagePrincipal.setMinWidth(980);
                stagePrincipal.setMinHeight(720);
            }

            stagePrincipal.sizeToScene();
            stagePrincipal.centerOnScreen();
            stagePrincipal.show();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao trocar de tela: " + fxml, e);
        }
    }
}