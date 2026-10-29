
public class Produto {
    private int id;
    private String nome;
    private String sku;
    private double preco;
    private int quantidadeEstoque;
    private boolean ativo;

    // Construtor para inserção de um novo produto
    public Produto(String nome, String sku, double preco,
                   int quantidadeEstoque) {
        this.nome = nome;
        this.sku = sku;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.ativo = true;
    }

    // Construtor para recuperação do banco de dados
    public Produto(int id, String nome, String sku, double preco,
                   int quantidadeEstoque, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.sku = sku;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sku='" + sku + '\'' +
                ", preco=" + preco +
                ", quantidadeEstoque=" + quantidadeEstoque +
                ", ativo=" + ativo +
                '}';
    }
}
