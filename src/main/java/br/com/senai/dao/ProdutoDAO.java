package br.com.senai.dao;

import br.com.senai.model.Produto;

// IMPORTAÇÕES NECESSÁRIAS PARA CONEXÃO COM O BANCO DE DADOS
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// CLASSE PRODUTODAO
// RESPONSÁVEL POR TODA A COMUNICAÇÃO ENTRE A CLASSE PRODUTO E A TABELA `PRODUTO` NO MYSQL
public class ProdutoDAO {

    // INSERE UM NOVO PRODUTO NO BANCO DE DADOS MYSQL, USANDO SQL PREPARADO PARA EVITAR SQL INJECTION
    // O METODO RETORNA O MESMO OBJETO PRODUTO, MAS COM O ID GERADO PELO MYSQL (AUTO_INCREMENT)
    public Produto salvar(Produto produto) {
        String sql = "INSERT INTO produto (nome, descricao, preco, quantidade_estoque) VALUES (?, ?, ?, ?)";

        // "try-with-resources" GARANTE QUE A CONEXÃO COM O BANCO DE DADOS SEJA FECHADA AUTOMATICAMENTE
        // MESMO SE OCORRER UM ERRO DURANTE A EXECUÇÃO DO CÓDIGO
        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // USAMOS "?" PARA EVITAR SQL INJECTION, E SETAMOS OS VALORES DO OBJETO PRODUTO NOS PARÂMETROS PREPARADOS
            // SQL INJECTION É UM TIPO DE ATAQUE EM QUE UM INVASOR PODE INSERIR CÓDIGO MALICIOSO EM UMA CONSULTA SQL
            // SE O CÓDIGO NÃO FOR PROTEGIDO. USANDO PARÂMETROS PREPARADOS, O JDBC ESCAPA AUTOMATICAMENTE OS CARACTERES ESPECIAIS
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidadeEstoque());

            stmt.executeUpdate();

            // RESULTSET É UMA ESTRUTURA DE DADOS QUE REPRESENTA O RESULTADO DE UMA CONSULTA SQL
            // AQUI ELE RECUPERA A CHAVE GERADA PELO MYSQL (O ID DO PRODUTO INSERIDO)
            try (ResultSet chavesGeradas = stmt.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    produto.setId(chavesGeradas.getInt(1));
                }
            }

            return produto;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage(), e);
        }
    }

    // BUSCA UM PRODUTO NO BANCO DE DADOS PELO ID, RETORNANDO UM OBJETO PRODUTO OU NULL SE NÃO ENCONTRAR
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produto WHERE id = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage(), e);
        }
    }

    // RETORNA TODOS OS PRODUTOS CADASTRADOS NO BANCO DE DADOS, ORDENADOS PELO NOME
    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM produto ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
        return produtos;
    }

    // ATUALIZA OS DADOS DE UM PRODUTO EXISTENTE NO BANCO DE DADOS, IDENTIFICADO PELO ID
    // USADO, POR EXEMPLO, DEPOIS DE UMA VENDA, PARA GRAVAR A BAIXA DE ESTOQUE
    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, quantidade_estoque = ? WHERE id = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        }
    }

    // MAPEIA UM RESULTSET PARA UM OBJETO PRODUTO
    // ISSO É ÚTIL PARA EVITAR REPETIÇÃO DE CÓDIGO, POIS TANTO "buscarPorId" QUANTO "listarTodos" PRECISAM FAZER ESSA CONVERSÃO
    private Produto mapearProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getDouble("preco"),
                rs.getInt("quantidade_estoque")
        );
    }
}
