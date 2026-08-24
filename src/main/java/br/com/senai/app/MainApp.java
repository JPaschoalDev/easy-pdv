package br.com.senai.app;

import br.com.senai.dao.UsuarioDAO;
import br.com.senai.model.Perfil;
import br.com.senai.model.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stagePrincipal) throws Exception {

        // REGISTRA O STAGE NO "NavegadorApp" ANTES DE CARREGAR A PRIMEIRA TELA
        NavegadorApp.inicializar(stagePrincipal);

        Parent raiz = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene cena = new Scene(raiz);

        stagePrincipal.setTitle("Easy.PDV - Login");
        stagePrincipal.setScene(cena);
        stagePrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}