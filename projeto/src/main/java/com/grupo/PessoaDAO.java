public class PessoaDAO implements AutoCloseable {
    private final Connection conexao;

    public PessoaDAO(String nomeBanco){
        try{
            Path caminho = Path.of("db", nomeBanco + ".db").toAbsolutePath();
            Files.createDirectories(caminho.getParent());
            conexao = DriverManager.getConnection("jdbc:sqlite:" + caminho);
        } catch (IOException | SQLEXception e){
            throw new IllegalStateEXception("Falha ao abrir o branco", e);
        }
        try {
            criarTabela();
        } catch (RuntimeException erro) {
            try {
                conexao.close();
            } catch (SQLException falhaAoFechar){
                erro.addSuppressed(falhaAoFechar);
            }
            throw erro;
        }

    }
    private void criarTabela(){
        String sql ="""
        CREATE TABLE IF NOT EXISTS pessoas (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        nome TEXT NOT NULL,
        idade INTEGER NOT NULL CHECK (idade >= 0),
        altura REAL NOT NULL CHECK (altura > 0)
        )
        """;
        try(Statement stmt = conexao.createStatement()){
            stmt.executeUpdate(sql);
        } catch (SQLException e){
            throw new IllegalStateException("Falha ao criar a tabela",e);
        }
    }
    public int inserirPessoa(Pessoa pessoa){
        String sql = "INSERT INTO pessoas (nome, idade, altura) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(
            sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, pessoa.getNome());
                ps.setInt(2, pessoa.getIdade());
                ps.setDouble(3, pessoa.getAltura());
                ps.executeUpdate();
            }
    }

        try(ResultSet = ps.getGeneratedKeys()){
            if (chaves.next()){
                int id = chaves.getInt(1);
                pessoa.setId(id);
                return id;
            }
        }
        throw new IllegalStateException("ID da pessoa não retornado");
    }  catch (SQLException e){
        throw new IllegalStateException("Falha ao inserir pessoa",e);
    }

public Pessoa buscarPorId(int id){
    String sql = "SELECT id,nome,idade,altura FROM pessoas WHERE id =?";
    try (PreparedStatement ps = conexao.prepareStatement(sql)){
        ps.setInt(1,id);
        try(ResultSet rs =ps.executeQuery()){
            return rs.next()? mapearPessoa(rs): null;
        }
    }catch (SQLException e ){
        throw new IllegalStateException("Falha ao buscar pessoa",e);
    }
}

public List<Pessoa> listarTodas(){
    List<Pessoa> pessoas = new ArrayList<>();
    String sql = "SELECT id, nome, idade, altura FROM pessoas ORDER BY id";
    try (PreparedStatement ps = conexao.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
       while (rs.next()){
        pessoas.add(mapearPessoa(rs));
       }
       return pessoas;
        } catch (SQLException e) {
            throw new IllegalStateExeception("Falha ao listar pessoas", e);
        }
    }
private Pessoa mapearPessoa(ResultSet rs) throws SQLException{
    return new  Pessoa(rs.getInt("id"), rs.getString("nome"),
            rs.getInt("idade"), rs.getDouble("altura"));
}