
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements AutoCloseable {

    private final Connection conexao;

    public ProdutoDAO(String url) throws SQLException {
        this.conexao = DriverManager.getConnection(url);
        criarTabela();
    }

    private void criarTabela() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS produtos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                sku TEXT NOT NULL UNIQUE,
                preco REAL NOT NULL CHECK (preco > 0),
                quantidade_estoque INTEGER NOT NULL
                    CHECK (quantidade_estoque >= 0),
                ativo INTEGER NOT NULL DEFAULT 1
                    CHECK (ativo IN (0, 1))
            )
            """;

        try (Statement stmt = conexao.createStatement()) {
            stmt.execute(sql);
        }
    }

    // CREATE: insere um novo produto
    public void salvar(Produto produto) throws SQLException {
        String sql = """
            INSERT INTO produtos
                (nome, sku, preco, quantidade_estoque, ativo)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt =
                     conexao.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getSku());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.isAtivo() ? 1 : 0);

            stmt.executeUpdate();
        }
    }

    // READ: busca por ID
    public Produto buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT id, nome, sku, preco, quantidade_estoque, ativo
            FROM produtos
            WHERE id = ?
            """;

        try (PreparedStatement stmt =
                     conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }

        return null;
    }

    // READ: busca por SKU
    public Produto buscarPorSku(String sku) throws SQLException {
        String sql = """
            SELECT id, nome, sku, preco, quantidade_estoque, ativo
            FROM produtos
            WHERE sku = ?
            """;

        try (PreparedStatement stmt =
                     conexao.prepareStatement(sql)) {

            stmt.setString(1, sku);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }

        return null;
    }

    // READ: lista somente os produtos ativos
    public List<Produto> listarAtivos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();

        String sql = """
            SELECT id, nome, sku, preco, quantidade_estoque, ativo
            FROM produtos
            WHERE ativo = 1
            ORDER BY id
            """;

        try (PreparedStatement stmt =
                     conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }

        return produtos;
    }

    // UPDATE: altera somente a quantidade em estoque
    public boolean atualizarEstoque(
            int id, int novaQuantidade) throws SQLException {

        if (novaQuantidade < 0) {
            throw new IllegalArgumentException(
                    "A quantidade em estoque não pode ser negativa.");
        }

        String sql = """
            UPDATE produtos
            SET quantidade_estoque = ?
            WHERE id = ?
            """;

        try (PreparedStatement stmt =
                     conexao.prepareStatement(sql)) {

            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    // SOFT DELETE: desativa sem remover o registro
    public boolean desativarProduto(int id) throws SQLException {
        String sql = """
            UPDATE produtos
            SET ativo = 0
            WHERE id = ? AND ativo = 1
            """;

        try (PreparedStatement stmt =
                     conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    // Converte uma linha do banco em um objeto Produto
    private Produto mapearProduto(ResultSet rs)
            throws SQLException {

        return new Produto(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("sku"),
                rs.getDouble("preco"),
                rs.getInt("quantidade_estoque"),
                rs.getInt("ativo") == 1
        );
    }

    // Fecha a conexão automaticamente com try-with-resources
    @Override
    public void close() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }
}
