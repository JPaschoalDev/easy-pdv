package br.com.senai.dao;

// IMPORTAÇÕES NECESSÁRIAS PARA CONEXÃO COM O BANCO DE DADOS
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// CLASSE CONEXAOFACTORY
// RESPONSÁVEL POR CRIAR E FORNECER CONEXÕES COM O BANCO MYSQL
// CENTRALIZADO AQUI POR MOTIVOS DE REPEITIVIDADE, PARA NÃO REPETIR URL, USUÁRIO E SENHA EM CADA DAO
public class ConexaoFactory {

    private static final String URL;
    private static final String USUARIO;
    private static final String SENHA;

    // CARREGA AS CONFIGURAÇÕES DO BANCO DE DADOS A PARTIR DE UM ARQUIVO .PROPERTIES
    static {
        Properties props = new Properties();
        try (InputStream input = ConexaoFactory.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                throw new RuntimeException(
                        "Arquivo database.properties não encontrado em src/main/resources/. " +
                                "Copie database.properties.example, renomeie e preencha com sua senha real."
                );
            }
            props.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler database.properties: " + e.getMessage(), e);
        }

        URL = props.getProperty("db.url");
        USUARIO = props.getProperty("db.usuario");
        SENHA = props.getProperty("db.senha");
    }

    // ABRE UMA NOVA CONEXÃO COM O BANCO DE DADOS MYSQL
    // CADA CLASSE "dao" DEVE CHAMAR ESSE METODO, USAR A CONEXÃO, E SEMPRE FECHÁ-LA DEPOIS (USAMOS TRY-WITH-RESOURCES PARA ISSO)
    public static Connection criarConexao() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
}