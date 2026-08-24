package br.com.senai.dao;

import br.com.senai.model.Perfil;
import br.com.senai.model.Usuario;

// IMPORTAÇÕES NECESSÁRIAS PARA CONEXÃO COM O BANCO DE DADOS
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// CLASSE USUARIODAO
// RESPONSÁVEL POR TODA A COMUNICAÇÃO ENTRE A CLASSE "Usuario" E A TABELA "usuario" NO MYSQL
public class UsuarioDAO {

    // INSERE UM NOVO USUÁRIO NO BANCO
    // A SENHA EM TEXTO PURO JÁ FOI TRANSFORMADA EM HASH, ESSA CLASSE NÃO ACESSA A SENHA EM TEXTO PURO
    public Usuario salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha_hash, perfil) VALUES (?, ?, ?, ?)";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenhaHash());
            stmt.setString(4, usuario.getPerfil().name());

            stmt.executeUpdate();

            try (ResultSet chavesGeradas = stmt.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    usuario.setId(chavesGeradas.getInt(1));
                }
            }

            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

    // BUSCA USUÁRIO PELO ID, RETORNA NULL SE NÃO ENCONTRAR
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e);
        }
    }

    // BUSCA USUÁRIO PELO E-MAIL, A TELA DE LOGIN VAI ÚTILIZAR ESSE METODO
    // PRIMEIRO BUSCA O USUÁRIO PELO EMAIL DIGITADO
    // SEGUNDO CHECA COM O "usuario.verificarSenha(senhaDigitada)" SE A SENHA EM HASH BATE COM A DO BANCO
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por e-mail: " + e.getMessage(), e);
        }
    }

    // RETORNA TODOS OS USUÁRIOS CADASTRADOS (TELA PARA GERENCIAR USUÁRIOS - APENAS PARA ADMIN)
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario ORDER BY nome";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }

        return usuarios;
    }

    // CONVERTE UMA LINHA DO "ResultSet" EM UM OBJETO "Usuario"
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("senha_hash"),
                Perfil.valueOf(rs.getString("perfil"))
        );
    }
}