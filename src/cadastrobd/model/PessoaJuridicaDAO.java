package cadastrobd.model;

import cadastrobd.model.util.ConectorBD;
import cadastrobd.model.util.SequenceManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Classe reposnsável por realizar as operações CRUD com o banco de dados
// para objetos do tipo PessoaJuridica.
public class PessoaJuridicaDAO {
    // Recupera uma pessoa jurídica de acordo com o ID informado.
    public PessoaJuridica getPessoa(int id) throws SQLException {
        String sql = "SELECT p.*, pj.cnpj FROM Pessoa p INNER JOIN PessoaJuridica pj ON p.idPessoa = pj.idPessoa WHERE p.idPessoa = ?";
        
        Connection conexao = null;
        PreparedStatement declaracao = null;
        ResultSet conjuntoResultado = null;
        
        try {
            conexao = ConectorBD.getConnection();
            declaracao = ConectorBD.getPrepared(conexao, sql);
            declaracao.setInt(1, id);
            conjuntoResultado = ConectorBD.getSelect(declaracao);
            
            if (conjuntoResultado.next()) {
                return new PessoaJuridica(
                        conjuntoResultado.getInt("idPessoa"),
                        conjuntoResultado.getString("nome"),
                        conjuntoResultado.getString("logradouro"),
                        conjuntoResultado.getString("cidade"),
                        conjuntoResultado.getString("estado"),
                        conjuntoResultado.getString("telefone"),
                        conjuntoResultado.getString("email"),
                        conjuntoResultado.getString("cnpj")
                );
            }
        }
        catch (SQLException e) {
            System.err.println("Erro ao tentar recuperar pessoa jurídica: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracao, conjuntoResultado);
        }
        
        return null;
    }
    
    // Recupera todas as pessoas jurídicas do banco de dados.
    public List<PessoaJuridica> getPessoas() throws SQLException {
        // Array que receberá as pessoas retornadas pelo banco de dados.
        List<PessoaJuridica> pessoas = new ArrayList<>();
        
        Connection conexao = null;
        PreparedStatement declaracao = null;
        ResultSet conjuntoResultado = null;
        
        String sql = "SELECT p.*, pj.cnpj FROM Pessoa p INNER JOIN PessoaJuridica pj ON p.idPessoa = pj.idPessoa";
        
        try {
            conexao = ConectorBD.getConnection();
            declaracao = ConectorBD.getPrepared(conexao, sql);
            conjuntoResultado = ConectorBD.getSelect(declaracao);
            
            while (conjuntoResultado.next()) {
                PessoaJuridica pessoa = new PessoaJuridica(
                        conjuntoResultado.getInt("idPessoa"),
                        conjuntoResultado.getString("nome"),
                        conjuntoResultado.getString("logradouro"),
                        conjuntoResultado.getString("cidade"),
                        conjuntoResultado.getString("estado"),
                        conjuntoResultado.getString("telefone"),
                        conjuntoResultado.getString("email"),
                        conjuntoResultado.getString("cnpj")
                );
                
                pessoas.add(pessoa);
            }
        }
        catch (SQLException e) {
            System.err.println("Erro ao recuperar pessoas jurídicas: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracao, conjuntoResultado);
        }
        
        return pessoas;
    }
    
    // Inclui uma nova pessoa jurídica no banco de dados.
    public void incluir(PessoaJuridica pessoa) throws SQLException {
        Connection conexao = null;
        PreparedStatement declaracaoPessoa = null;
        PreparedStatement declaracaoPessoaJuridica = null;
        
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
            
            // Inserindo na tabela PessoaJuridica.
            String sqlPessoaJuridica = "INSERT INTO PessoaJuridica(idPessoa, cnpj) VALUES(?, ?)";
            declaracaoPessoaJuridica = ConectorBD.getPrepared(conexao, sqlPessoaJuridica);
            declaracaoPessoaJuridica.setInt(1, id);
            declaracaoPessoaJuridica.setString(2, pessoa.getCnpj());
            declaracaoPessoaJuridica.executeUpdate();
            
            // Confirmando a transação com o banco de dados.
            conexao.commit();            
        }
        catch (SQLException e) {
            if (conexao != null) {
                try {
                    // Desfazendo as alterações realizadas.
                    conexao.rollback();
                }
                catch (SQLException ex) {
                    System.err.println("Erro ao tentar encerrar a conexão: " + ex.getMessage());
                }
            }
            
            System.err.println("Erro ao tentar incluir pessoa jurídica: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracaoPessoa);
            ConectorBD.close(declaracaoPessoaJuridica);
        }
    }
    
    // Altera uma pessoa jurídica no banco de dados.
    public void alterar(PessoaJuridica pessoa) throws SQLException {
        Connection conexao = null;
        PreparedStatement declaracaoPessoa = null;
        PreparedStatement declaracaoPessoaJuridica = null;
        
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
            
            // Alterando registro da tabela PessoaJuridica.
            String sqlPessoaJuridica = "UPDATE PessoaJurirca SET cnpj = ? WHERE idPessoa = ?";
            declaracaoPessoaJuridica = ConectorBD.getPrepared(conexao, sqlPessoaJuridica);
            declaracaoPessoaJuridica.setString(1, pessoa.getCnpj());
            declaracaoPessoaJuridica.setInt(2, pessoa.getId());
            
            // Confirmando a transação.
            conexao.commit();            
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
            
            System.err.println("Erro ao tentar alterar a pessoa jurídica: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracaoPessoa);
            ConectorBD.close(declaracaoPessoaJuridica);
        }
    }
    
    // Exclui uma pessoa jurídica do banco de dados, de acordo com o ID informado.
    public void excluir(int id) throws SQLException {
        Connection conexao = null;
        PreparedStatement declaracaoPessoa = null;
        PreparedStatement declaracaoPessoaJuridica = null;
        
        try {
            // Conectando ao banco de dados.
            conexao = ConectorBD.getConnection();
            // Iniciando a transação.
            conexao.setAutoCommit(false);
            
            // Excluindo registro da tabela PessoaJuridica.
            String sqlPessoaJuridica = "DELETE FROM PessoaJuridica WHERE idPessoa = ?";
            declaracaoPessoaJuridica = ConectorBD.getPrepared(conexao, sqlPessoaJuridica);
            declaracaoPessoaJuridica.setInt(1, id);
            declaracaoPessoaJuridica.executeUpdate();
            
            // Excluindo registro da tabela Pessoa.
            String sqlPessoa = "DELETE FROM Pessoa WHERE idPessoa = ?";
            declaracaoPessoa = ConectorBD.getPrepared(conexao, sqlPessoa);
            declaracaoPessoa.setInt(1, id);
            declaracaoPessoa.executeUpdate();
            
            // Confirmando a transação.
            conexao.commit();            
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
            
            System.err.println("Erro ao tentar excluir pessoa jurídica: " + e.getMessage());
        }
        finally {
            ConectorBD.close(conexao, declaracaoPessoa);
            ConectorBD.close(declaracaoPessoaJuridica);
        }
    }
}
