package cadastrobd;

import cadastrobd.model.PessoaFisica;
import cadastrobd.model.PessoaFisicaDAO;
import cadastrobd.model.PessoaJuridica;
import cadastrobd.model.PessoaJuridicaDAO;
import java.sql.SQLException;

public class CadastroBDTeste {
    public static void main(String[] args) throws SQLException {
        // a) Instanciar uma pessoa física e persistir no banco de dados.
        PessoaFisica pessoaFisica = new PessoaFisica(
                0, // A sequência do banco de dados cuidará de atribuir o ID correto.
                "João Silva",
                "Rua das Flores, nº 123",
                "São Paulo",
                "SP",
                "(11) 98765-4321",
                "joao@email.com",
                "12345678900"
        );
        
        PessoaFisicaDAO pfDAO = new PessoaFisicaDAO();
        
        pfDAO.incluir(pessoaFisica);
        
        // b) Alterar os dados da pessoa física no banco.
        pessoaFisica.setLogradouro("Rua dos Cravos, nº 456");
        
        pfDAO.alterar(pessoaFisica);
        
        // c) Consultar todas as pessoas físicas do banco de dados e listar no console.
        pfDAO.getPessoas();
        
        // d) Excluir a pessoa física criada anteriormente no banco.
        pfDAO.excluir(pessoaFisica.getId());
        
        // e) Instanciar uma pessoa jurídica e persistir no banco de dados.
        PessoaJuridica pessoaJuridica = new PessoaJuridica(
                0, // A sequência do banco de dados cuidará de atribuir o ID correto.
                "Tech Solutions Ltda.",
                "Rua das Empresas, nº 789",
                "Rio de Janeiro",
                "RJ",
                "(21) 98123-4567",
                "contato@techsolutions.com",
                "12345678000199"
        );
        
        PessoaJuridicaDAO pjDAO = new PessoaJuridicaDAO();
        
        pjDAO.incluir(pessoaJuridica);
        
        // f) Alterar os dados da pessoa jurídica no banco.
        pessoaJuridica.setNome("Tech Solutions S.A.");
        pessoaJuridica.setLogradouro("Avenida das Empresas, nº 321");
        
        pjDAO.alterar(pessoaJuridica);
        
        // g) Consultar todas as pessoas jurídicas do banco e listar no console.
        pjDAO.getPessoas();
        
        // h) Excluir a pessoa jurídica criada anteriormente no banco. 
        pjDAO.excluir(pessoaJuridica.getId());
    }
}
