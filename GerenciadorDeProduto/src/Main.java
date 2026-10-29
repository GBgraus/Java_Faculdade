
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:produtos.db";

        try (ProdutoDAO dao = new ProdutoDAO(url)) {

            // CREATE
            Produto produto = new Produto(
                    "Notebook",
                    "NOTE-001",
                    3499.90,
                    10
            );

            dao.salvar(produto);
            System.out.println("Produto cadastrado.");

            // READ: localizar pelo SKU
            Produto encontrado = dao.buscarPorSku("NOTE-001");

            if (encontrado != null) {
                System.out.println("Encontrado: " + encontrado);

                // UPDATE: atualizar estoque usando o ID real
                boolean atualizado = dao.atualizarEstoque(
                        encontrado.getId(), 15
                );

                System.out.println(
                        "Estoque atualizado: " + atualizado
                );

                // SOFT DELETE
                // Descomente para desativar o produto:
                // dao.desativarProduto(encontrado.getId());
            }

            // READ: listar produtos ativos
            List<Produto> produtos = dao.listarAtivos();

            System.out.println("\nProdutos ativos:");

            for (Produto p : produtos) {
                System.out.println(p);
            }

        } catch (SQLException e) {
            System.err.println(
                    "Erro ao acessar o banco: " + e.getMessage()
            );
        }
    }
}
