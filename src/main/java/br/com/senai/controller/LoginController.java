package br.com.senai.controller;

import br.com.senai.app.NavegadorApp;
import br.com.senai.dao.UsuarioDAO;
import br.com.senai.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

// CONTROLER DA TELA DE LOGIN
// CADA ATRIBUITO ABAIXO MARCADO COM UM "@FXML" CORRESPONDE A UM COMPONENTE DO FXML QUE TENHA O MESMO "fx:id"
// NO SCENE BUILDER O "fx:id" PRECISA SER O MESMO QUE AQUI, CASO CONTRÁRIO A CONEXÃO FALHA
public class LoginController {

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private Button botaoEntrar;

    @FXML
    private Label labelMensagemErro;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // METODO CHAMADO QUANDO O BOTÃO "Entrar" É CLICADO
    // NO SCENE BUILDER FOI DEFINIDO O "Button" COMO "#autenticar" NO "On Action"
    @FXML
    private void autenticar() {
        String email = campoEmail.getText();
        String senha = campoSenha.getText();

        labelMensagemErro.setText("");

        if (email.isBlank() || senha.isBlank()) {
            labelMensagemErro.setText("PREENCHE E-MAIL e SENHA");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null || !usuario.verificarSenha(senha)) {
            // POR SEGURANÇA A MENSAGEM DE ERRO É PADRÃO
            labelMensagemErro.setText("E-MAIL OU SENHA INVÁLIDOS. TENTE NOVAMENTE");
            return;
        }
        // LOGIN EFETUADO SEM FALHAS, GUARDA O TIPO DE USUÁRIO LOAGDO
        NavegadorApp.setUsuarioLogado(usuario);
        NavegadorApp.trocarTela("/fxml/principal.fxml", "Easy.PDV - Início");
    }
}