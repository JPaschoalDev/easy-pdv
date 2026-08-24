package br.com.senai.dao;

import br.com.senai.model.*;

// IMPORTAÇÕES NECESSÁRIAS PARA CONEXÃO COM O BANCO DE DADOS
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

// CLASSE VENDADAO
// RESPONSÁVEL POR TODA A COMUNICAÇÃO ENTRE A CLASSE "Venda" E A TABELA "venda" NO MYSQL

// A VENDA ENVOLVE DUAS TABELAS: "venda" E "item_venda", POR CONTA DISSO QUE "salvar()" USA UMA TRANSAÇÃO:
// OU GRAVA TODA A VENDA OU NADA É GRAVADO COM SUCESSO (VENDA + TODOS OS ITENS)
public class VendaDAO {

    public Venda salvar(Venda venda) {
        String sqlVenda = "INSERT INTO venda (cliente_id, usuario_id, data_venda, valor_total, forma_pagamento) " +
                "VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_venda (venda_id, produto_id, quantidade, preco_unitario) " +
                "VALUES (?, ?, ?, ?)";

        Connection conexao = null;

        try {
            conexao = ConexaoFactory.criarConexao();
            // DESLIGA O "auto-commit", A PARTIR DESTE PONTO NADA É GRAVADO NO BANCO ATÉ CHAMAR O "conexao.commit()"
            conexao.setAutoCommit(false);

            // 1° PASSO: GRAVA O CABEÇALHO DA VENDA
            int vendaId;
            try (PreparedStatement stmt = conexao.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, venda.getCliente().getId());
                stmt.setInt(2, venda.getVendedor().getId());
                stmt.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda()));
                stmt.setDouble(4, venda.getValorTotal());
                stmt.setString(5, venda.getFormaPagamento().name());

                stmt.executeUpdate();

                try (ResultSet chavesGeradas = stmt.getGeneratedKeys()) {
                    chavesGeradas.next();
                    vendaId = chavesGeradas.getInt(1);
                    venda.setId(vendaId);
                }
            }
            // 2° PASSO: GRAVA CADA ITEM DO CARRINHO, ASSOCIADO COM O ID DA VENDA A CIMA
            try (PreparedStatement stmt = conexao.prepareStatement(sqlItem)) {
                for (ItemVenda item : venda.getItens()) {
                    stmt.setInt(1, vendaId);
                    stmt.setInt(2, item.getProduto().getId());
                    stmt.setInt(3, item.getQuantidade());
                    stmt.setDouble(4, item.getPrecoUnitario());
                    stmt.addBatch();
                }
                stmt.executeBatch(); // EXECUTA TODOS OS INSERT´S DE ITEM DE UMA VEZ
            }
            // 3° PASSO: SE CHEGAMOS ATÉ AQUI SEM ESCEÇÃO, CONFIRMA TUDO DE UMA VEZ
            // RESPEITANDO ASSIM A REGRA DO NÉGOCIO E EVITANDO ERROS INVISÍVEIS
            conexao.commit();

            return venda;

        } catch (SQLException e) {

            // CASO HAJA ALGUM ERRO NO PASSO A PASSO A CIMA, TUDO É DESFEITO DESSA TRANSAÇÃO
            // NENHUM DADO SALVO PELÇA METADE
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao desfazer transação de venda: " + ex.getMessage(), ex);
                }
            }
            throw new RuntimeException("Erro ao salvar venda: " + e.getMessage(), e);

        } finally {
            // INDEPENDENTE DE ERRO OU NÃO, FECGAMOS A CONEXÃO AO FIM
            if (conexao != null) {
                try {
                    conexao.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage(), e);
                }
            }
        }
    }

    // RETORNA TODAS AS VENDAS CADASTRADAS, APENAS O CABEÇALHO, SEM OS ITENS
    public List<Venda> listarTodas(ClienteDAO clienteDAO, UsuarioDAO usuarioDAO) {
        String sql = "SELECT * FROM venda ORDER BY data_venda DESC";
        List<Venda> vendas = new ArrayList<>();

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
                Usuario vendedor = usuarioDAO.buscarPorId(rs.getInt("usuario_id"));
                FormaPagamento forma = FormaPagamento.valueOf(rs.getString("forma_pagamento"));

                Venda venda = new Venda(cliente, vendedor, forma);
                venda.setId(rs.getInt("id"));
                vendas.add(venda);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vendas: " + e.getMessage(), e);
        }

        return vendas;
    }
}