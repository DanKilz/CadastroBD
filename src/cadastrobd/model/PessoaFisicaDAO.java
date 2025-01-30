package cadastrobd.model;

import cadastrobd.model.util.ConectorBD;
import cadastrobd.model.util.SequenceManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaFisicaDAO {
    /**
     * Recupera uma pessoa física de acordo com o ID informado.
     * @param id ID da pessoa física a ser buscada.
     * @return objeto PessoaFisica ou null, se a pessoa não for encontrada.
     * @throws SQLException caso a pessoa física não seja econtrada.
    */
    public PessoaFisica getPessoa(int id) throws SQLException {
        String sql = "SELECT p.*, pf.cpf FROM Pessoa p INNER JOIN PessoaFisica pf ON p.idPessoa = pf.idPessoa WHERE idPessoa = ?";
                
        Connection conexao = null;
        PreparedStatement declaracao = null;
        ResultSet conjuntoResultado = null;
        
        try {
            conexao = ConectorBD.getConnection();
            declaracao = ConectorBD.getPrepared(conexao, sql);
            declaracao.setInt(1, id);
            conjuntoResultado = ConectorBD.getSelect(declaracao);
            
            if (conjuntoResultado.next()) {
                return new PessoaFisica(
                        conjuntoResultado.getInt("idPessoa"),
                        conjuntoResultado.getString("nome"),
                        conjuntoResultado.getString("logradouro"),
                        conjuntoResultado.getString("cidade"),
                        conjuntoResultado.getString("estado"),
                        conjuntoResultado.getString("telefone"),
                        conjuntoResultado.getString("email"),
                        conjuntoResultado.getString("cpf")
                );
            }
        }
        catch (SQLException e) {
            System.err.println("Erro ao recuperar pessoa física: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracao, conjuntoResultado);
        }
        
        return null;
    }
    
    /**
     * Recupera todas as pessoas físicas do banco de dados.
     * @return Lista de pessoas físicas.
     * @throws SQLException caso não seja possível recuperar todas as pessoas físicas.
    */
    public List<PessoaFisica> getPessoas() throws SQLException {
        // Array que receberá as pessoas retornadas pelo banco de dados.
        List<PessoaFisica> pessoas = new ArrayList<>();
        
        Connection conexao = null;
        PreparedStatement declaracao = null;
        ResultSet conjuntoResultado = null;
        
        String sql = "SELECT p.*, pf.cpf FROM Pessoa p INNER JOIN PessoaFisica pf ON p.idPessoa = pf.idPessoa";
        
        try {
            conexao = ConectorBD.getConnection();
            declaracao = ConectorBD.getPrepared(conexao, sql);
            conjuntoResultado = ConectorBD.getSelect(declaracao);
            
            while (conjuntoResultado.next()) {
                PessoaFisica pessoa = new PessoaFisica(
                        conjuntoResultado.getInt("idPessoa"),
                        conjuntoResultado.getString("nome"),
                        conjuntoResultado.getString("logradouro"),
                        conjuntoResultado.getString("cidade"),
                        conjuntoResultado.getString("estado"),
                        conjuntoResultado.getString("telefone"),
                        conjuntoResultado.getString("email"),
                        conjuntoResultado.getString("cpf")
                );
                
                pessoas.add(pessoa);
            }
            
            System.out.println("\n==============================");
            System.out.println("PESSOAS FÍSICAS CADASTRADAS:");
            for (PessoaFisica pessoa : pessoas) {
                pessoa.exibir();
            }
            System.out.println("==============================\n");
        }
        catch (SQLException e) {
            System.err.println("Erro ao recuperar pessoas físicas: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracao, conjuntoResultado);
        }
        
        return pessoas;
    }
    
    /**
     * Inclui uma nova pessoa física no banco de dados.
     * @param pessoa Pessoa física a ser incluída.
     * @throws SQLException Se não for possível incluir a pessoa.
    */
    public void incluir(PessoaFisica pessoa) throws SQLException {
        Connection conexao = null;
        PreparedStatement declaracaoPessoa = null;
        PreparedStatement declaracaoPessoaFisica = null;        
        
        try {
            // Conectando ao banco de dados.
            conexao = ConectorBD.getConnection();
            // Iniciando a transação com o banco de dados.
            conexao.setAutoCommit(false);
            
            // Obtendo o próximo ID da sequência criada no banco de dados.
            int id = SequenceManager.getValue("SeqIdPessoa");
            pessoa.setId(id); // O ID gerado pelo banco de dados é atribuído ao objeto carregado em memória.
            
            // Inserindo na tabela Pessoa.
            String sqlPessoa = "INSERT INTO Pessoa(idPessoa, nome, logradouro, cidade, estado, telefone, email) VALUES(?, ?, ?, ?, ?, ?, ?)";
            declaracaoPessoa = ConectorBD.getPrepared(conexao, sqlPessoa);
            declaracaoPessoa.setInt(1, id);
            declaracaoPessoa.setString(2, pessoa.getNome());
            declaracaoPessoa.setString(3, pessoa.getLogradouro());
            declaracaoPessoa.setString(4, pessoa.getCidade());
            declaracaoPessoa.setString(5, pessoa.getEstado());
            declaracaoPessoa.setString(6, pessoa.getTelefone());
            declaracaoPessoa.setString(7, pessoa.getEmail());
            declaracaoPessoa.executeUpdate();            
            
            // Inserindo na tabela PessoaFisica.
            String sqlPessoaFisica = "INSERT INTO PessoaFisica(idPessoa, cpf) VALUES(?, ?)";
            declaracaoPessoaFisica = ConectorBD.getPrepared(conexao, sqlPessoaFisica);
            declaracaoPessoaFisica.setInt(1, id);
            declaracaoPessoaFisica.setString(2, pessoa.getCpf());
            declaracaoPessoaFisica.executeUpdate();
            
            // Confirmando a transação com o banco de dados.
            conexao.commit();
            
            System.out.println("Pessoa física incluída com sucesso!");
        }
        catch (SQLException e) {
            if (conexao != null) {
                try {
                    // Desfazendo as alterações realizadas.
                    conexao.rollback();
                }
                catch (SQLException ex) {
                    System.err.println("Erro ao tentar encerrar conexão: " + ex.getMessage());
                }
            }
            
            System.err.println("Erro ao tentar incluir pessoa física: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracaoPessoa);
            ConectorBD.close(declaracaoPessoaFisica);
        }
    }
    
    /**
     * Altera uma pessoa física no banco de dados.
     * @param pessoa Pessoa física a ser alterada, com base em seu ID, que não será alterado.
     * @throws SQLException Se não for possível alterar a pessoa física.
    */
    public void alterar(PessoaFisica pessoa) throws SQLException {        
        Connection conexao = null;
        PreparedStatement declaracaoPessoa = null;
        PreparedStatement declaracaoPessoaFisica = null;        
        
        try {
            // Conectando ao banco de dados.
            conexao = ConectorBD.getConnection();
            // Iniciando a transação.
            conexao.setAutoCommit(false);
            
            // Alterando registro da tabela Pessoa.
            String sqlPessoa = "UPDATE Pessoa SET nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE idPessoa = ?";
            declaracaoPessoa = ConectorBD.getPrepared(conexao, sqlPessoa);
            declaracaoPessoa.setString(1, pessoa.getNome());
            declaracaoPessoa.setString(2, pessoa.getLogradouro());
            declaracaoPessoa.setString(3, pessoa.getCidade());
            declaracaoPessoa.setString(4, pessoa.getEstado());
            declaracaoPessoa.setString(5, pessoa.getTelefone());
            declaracaoPessoa.setString(6, pessoa.getEmail());
            declaracaoPessoa.setInt(7, pessoa.getId());
            declaracaoPessoa.executeUpdate();
                        
            // Alterando registro da tabela PessoaFisica.
            String sqlPessoaFisica = "UPDATE PessoaFisica SET cpf = ? WHERE idPessoa = ?";
            declaracaoPessoaFisica = ConectorBD.getPrepared(conexao, sqlPessoaFisica);
            declaracaoPessoaFisica.setString(1, pessoa.getCpf());
            declaracaoPessoaFisica.setInt(2, pessoa.getId());
            declaracaoPessoaFisica.executeUpdate();
            
            // Confirmando a transação.
            conexao.commit();
            
            System.out.println("Pessoa física alterada com sucesso!");
        }
        catch (SQLException e) {
            if (conexao != null) {
                try {
                    // Desfazendo as alterações realizadas.
                    conexao.rollback();
                }
                catch (SQLException ex) {
                    System.err.println("Erro ao tentar encerrar conexão: " + ex.getMessage());
                }
            }
            
            System.err.println("Erro ao tentar alterar a pessoa física: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracaoPessoa);
            ConectorBD.close(declaracaoPessoaFisica);
        }
    }
    
    /**
     * Exclui uma pessoa física do banco de dados, de acordo com o ID informado.
     * @param id ID da pessoa física a ser excluída.
     * @throws SQLException Se não for possível exluir a pessoa física.
    */
    public void excluir(int id) throws SQLException {
        Connection conexao = null;
        PreparedStatement declaracaoPessoa = null;
        PreparedStatement declaracaoPessoaFisica = null;
        
        try {
            // Conectando ao banco de dados.
            conexao = ConectorBD.getConnection();
            // Iniciando a transação.
            conexao.setAutoCommit(false);
            
            // Excluindo registro da tabela PessoaFisica.
            String sqlPessoaFisica = "DELETE FROM PessoaFisica WHERE idPessoa = ?";
            declaracaoPessoaFisica = ConectorBD.getPrepared(conexao, sqlPessoaFisica);
            declaracaoPessoaFisica.setInt(1, id);
            declaracaoPessoaFisica.executeUpdate();
            
            // Excluindo registro da tabela Pessoa.
            String sqlPessoa = "DELETE FROM Pessoa WHERE idPessoa = ?";
            declaracaoPessoa = ConectorBD.getPrepared(conexao, sqlPessoa);
            declaracaoPessoa.setInt(1, id);
            declaracaoPessoa.executeUpdate();
            
            // Confirmando a transação.
            conexao.commit();
            
            System.out.println("Pessoa física excluída com sucesso!");
        }
        catch (SQLException e) {
            if (conexao != null) {
                try {
                    // Desfazendo as alterações realizadas.
                    conexao.rollback();
                }
                catch (SQLException ex) {
                    System.err.println("Erro ao tentar desfazer a transação: " + ex.getMessage());
                }
            }
            
            System.err.println("Erro ao tentar excluir pessoa física: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracaoPessoa);
            ConectorBD.close(declaracaoPessoaFisica);
        }
    }
}
