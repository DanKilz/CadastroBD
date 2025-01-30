package cadastrobd.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConectorBD {
    // Dados para a conexão com o banco de dados.
    private static final String url = "jdbc:sqlserver://localhost:1433;databaseName=Loja;encrypt=true;trustServerCertificate=true";
    private static final String usuario = "loja";
    private static final String senha = "loja";
    
    // Métodos para realizar um conexão, criar uma consulta e receber o resultado da consulta realizada.
    
    /**
     * Obtém uma conexão com o banco de dados.
     * @return objeto Connection.
     * @throws SQLException se houver erro na conexão.
    */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, usuario, senha);
    }
    
    /**
     * Cria um PreparedStatement a partir de um comando SQL.
     * @param conexao Conexão a ser utilizada para criar o PreparedStatement.
     * @param sql Comando SQL.
     * @return PreparedStatement.
     * @throws SQLException se houver erro na criação do PreparedStatement.
    */
    public static PreparedStatement getPrepared(Connection conexao, String sql) throws SQLException {
        return conexao.prepareStatement(sql);
    }
    
    /**
     * Executa uma consulta SELECT e retorna o ResultSet correspondente.
     * @param declaracao PreparedStatement com o comando SQL a ser executado.
     * @return ResultSet com o resultado da consulta.
     * @throws SQLException se houver erro na execução da consulta.
    */
    public static ResultSet getSelect(PreparedStatement declaracao) throws SQLException {        
        return declaracao.executeQuery();
    }
    
    // Métodos de fechamento para as conexões e objetos criados.
    
    /**
     * Fecha uma conexão que não seja nula.
     * @param conexao Conexão a ser fechada.
    */
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
    
    /**
     * Fecha um PreparedStatement que não seja nulo.
     * @param declaracao PreparedStatement a ser fechado.
    */
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
    
    /**
     * Fecha um ResultSet que não seja nulo.
     * @param conjuntoResultado ResultSet a ser fechado.
    */
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
    
    
    /**
     * Fecha múltiplos recursos de acesso a banco de dados.
     * @param conexao Conexão a ser fechada.
     * @param declaracao PreparedStatement a ser fechado.
     * @throws SQLException 
    */
    public static void close(Connection conexao, PreparedStatement declaracao) throws SQLException {
        try {
            conexao.close();
            declaracao.close();
        }
        catch (SQLException e) {
            System.err.println("Erro ao tentar encerrar recursos: " + e.getMessage());
        }        
    }
    
    /**
     * Fecha múltiplos recursos de acesso a banco de dados.
     * @param conexao Conexão a ser fechada.
     * @param declaracao PreparedStatement a ser fechado.
     * @param conjuntoResultado ResultSet a ser fechado.
     * @throws SQLException 
    */
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
