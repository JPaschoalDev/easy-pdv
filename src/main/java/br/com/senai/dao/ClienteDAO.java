package br.com.senai.dao;

import br.com.senai.model.Cliente;

// IMPORTAÇÕES NECESSÁRIAS PARA CONEXÃO COM O BANCO DE DADOS
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// CLASSE CLIENTEDAO
// RESPONSÁVEL POR TODA A COMUNICAÇÃO ENTRE A CLASSE "Cliente" E A TABELA "cliente" NO MYSQL
public class ClienteDAO {

    // INSERE UM NOVO CLIENTE AO BANCO E DEVOLVE O MESMO JÁ COM ID GERADO
    // O CLIENTE PASSA PELA INTEGRAÇÃO COM O VIACEP QUE JÁ PREENCHE CORRETAMENTE O ENDEREÇO
    public Cliente salvar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, cpf, cep, logradouro, bairro, cidade, uf) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getCep());
            stmt.setString(4, cliente.getLogradouro());
            stmt.setString(5, cliente.getBairro());
            stmt.setString(6, cliente.getCidade());
            stmt.setString(7, cliente.getUf());

            stmt.executeUpdate();

            try (ResultSet chavesGeradas = stmt.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    cliente.setId(chavesGeradas.getInt(1));
                }
            }

            return cliente;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar Cliente: " + e.getMessage(), e);
        }
    }

    // BUSCA UM CLIENTE PELO ID DO BANCO DE DADOS, RETORNA NULL SE NÃO ENCONTRAR
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Cliente: " + e.getMessage(), e);
        }
    }

    // BUSCA UM CLIENTE PELO CPF, ÚTIL PARA CHECAR SE O CPF JÁ EXISTE NO BANCO
    public Cliente buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM cliente WHERE cpf = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Cliente por CPF: " + e.getMessage(), e);
        }
    }

    // RETORNA TODOS OS CLIENTES CADASTRADOS NO BANCO
    public List<Cliente> listarTodos() {
        String sql = "SELECT * FROM cliente ORDER BY nome";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }

        return clientes;
    }

    // CONVERTE UMA LINHA DO "ResultSet" em um objeto Cliente
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getString("cep"),
                rs.getString("logradouro"),
                rs.getString("bairro"),
                rs.getString("cidade"),
                rs.getString("uf")
        );
    }
}