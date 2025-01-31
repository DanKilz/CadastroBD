package cadastrobd.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Classe responsável pelas interações básicas com o banco de dados
// (conexão, consulta e recebimento de resultado da consulta).
public class ConectorBD {
    // ATRIBUTOS (constantes)
    
    // Dados para a conexão com o banco de dados.
    private static final String url = "jdbc:sqlserver://localhost:1433;databaseName=Loja;encrypt=true;trustServerCertificate=true";
    private static final String usuario = "loja";
    private static final String senha = "loja";
    
    // MÉTODOS
    
    // Obtém uma conexão com o banco de dados.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, senha);
    }
    
    // Cria um PreparedStatement a partir de uma determinada conexão
    // com o bando de dados.
    public static PreparedStatement getPrepared(Connection conexao, String sql) throws SQLException {
        return conexao.prepareStatement(sql);
    }
    
    // Executa uma consulta SELECT e retorna o resultado na forma de um ResultSet.
    public static ResultSet getSelect(PreparedStatement declaracao) throws SQLException {        
        return declaracao.executeQuery();
    }
    
    // Métodos de fechamento de recursos criados.
    
    // Fecha uma conexão que não seja nula.
    public static void close(Connection conexao) {
        try {
            if (conexao != null) {
            conexao.close();
            }
        }
        catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
    
    // Fecha um PreparedStatement que não seja nulo.
    public static void close(PreparedStatement declaracao) {
        try {
            if (declaracao != null) {
                declaracao.close();
            }
        }
        catch (SQLException e) {
            System.err.println("Erro ao fechar o PreparedStatement: " + e.getMessage());
        }
    }
    
    // Fecha um ResultSet que não seja nulo.
    public static void close(ResultSet conjuntoResultado) {
        try {
            if (conjuntoResultado != null) {
                conjuntoResultado.close();
            }
        }
        catch (SQLException e) {
            System.err.println("Erro ao fechar o ResultSet: " + e.getMessage());
        }
    }
    
    
    // Fecha múltiplos recursos de acesso a banco de dados.
    public static void close(Connection conexao, PreparedStatement declaracao) throws SQLException {
        try {
            conexao.close();
            declaracao.close();
        }
        catch (SQLException e) {
            System.err.println("Erro ao tentar encerrar recursos: " + e.getMessage());
        }        
    }
    
    // Fecha múltiplos recursos de acesso a banco de dados.
    public static void close(Connection conexao, PreparedStatement declaracao, ResultSet conjuntoResultado) throws SQLException {
        try {
            conexao.close();
            declaracao.close();
            conjuntoResultado.close();
        }
        catch (SQLException e) {
            System.err.println("Erro ao tentar encerrar recursos: " + e.getMessage());
        }        
    }
}
