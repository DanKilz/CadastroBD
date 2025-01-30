package cadastrobd.model.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SequenceManager {
    /**
     * Obtém o próximo valor de uma sequência no banco de dados.
     * @param nomeSequencia Nome da sequência.
     * @return Próximo valor da sequência.
     * @throws SQLException Se houver erro na consulta.
    */
    public static int getValue(String nomeSequencia) throws SQLException {
        String sql = "SELECT NEXT VALUE FOR " + nomeSequencia + " AS proximoValor";
        
        Connection conexao = null;
        PreparedStatement declaracao = null;
        ResultSet conjuntoResultado = null;
        
        try {
            conexao = ConectorBD.getConnection();
            declaracao = ConectorBD.getPrepared(conexao, sql);
            conjuntoResultado = declaracao.executeQuery();
            
            if (conjuntoResultado.next()) {
                return conjuntoResultado.getInt("proximoValor");
            }
            else {
                throw new SQLException("Erro ao obter o próximo valor da sequência: " + nomeSequencia);
            }
        }
        finally {
            ConectorBD.close(conexao, declaracao, conjuntoResultado);
        }        
    }
}
