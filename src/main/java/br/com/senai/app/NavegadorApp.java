package br.com.senai.app;

import br.com.senai.model.Usuario;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// CENTRALIZA A TROCA DE TELAS DO SISTEMA
// AO IVÉS DE CADA CONTROLLER SABER COMO TROCAR DE UMA TELA PARA OUTRA
// OS MÉTODOS ESTÁTICOS DESSA CLASSE SÃO CHAMADOS, ISSO TRONA O SISTEMA ESCALONÁVEL E DE FÁCIL MANUTENÇÃO
// AQUI TAMBÉM É GUARDADO O USUÁRIO ATUAL QUE ESTÁ LOGADO
public class NavegadorApp {

    private static Stage stagePrincipal;
    private static Usuario usuarioLogado;

    // METODO CHAMADO UMA ÚNICA VE PELO "MainApp" PARA GUARDAR A REFERÊNCIA DO STAGE PRINCIPAL
    // APARTIR DESSE PONTO QUALQUER CONTROLER DO SISTEMA PODE PEDIR PARA TROCAR DE TELA
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
    public static void trocarTela(String caminhoFxml, String titulo) {
        try {
            Parent raiz = FXMLLoader.load(NavegadorApp.class.getResource(caminhoFxml));
            Scene cena = new Scene(raiz);
            stagePrincipal.setScene(cena);
            stagePrincipal.setTitle(titulo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar a tela: " + caminhoFxml, e);
        }
    }
}
