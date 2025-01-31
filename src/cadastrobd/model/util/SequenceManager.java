package cadastrobd.model.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Classe responsável por lidar com a sequência criada no banco de dados.
// Essa sequência será responsável por gerar os números de ID dos registros incluídos.
public class SequenceManager {
    // Obtém o próximo valor de uma sequência no banco de dados.
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
