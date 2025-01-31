package cadastrobd.view;

import cadastrobd.model.PessoaFisica;
import cadastrobd.model.PessoaFisicaDAO;
import cadastrobd.model.PessoaJuridica;
import cadastrobd.model.PessoaJuridicaDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

// Classe responsável por implementar toda a lógica de interação com o usuário
// através da tela do console.
public class CadastroConsole {
    // ATRIBUTOS (constantes)
    private static final Scanner sc = new Scanner(System.in);
    private static final PessoaFisicaDAO pfDAO = new PessoaFisicaDAO();
    private static final PessoaJuridicaDAO pjDAO = new PessoaJuridicaDAO();
    
    // Construtor
    public CadastroConsole() {}
    
    // MÉTODOS
    
    // Inicia a interface com o usuário.
    public void iniciar() {
        int opcao;

        do {
            menu();
            opcao = lerOpcao();
            
            try {
                processarOpcao(opcao);
            }
            catch (SQLException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }
    
    // Exibe o menu de opções para o usuário.
    private static void menu() {
        System.out.println("\n===== SISTEMA DE CADASTRO DE PESSOAS =====");
        System.out.println("O que deseja fazer?");
        System.out.println("1 - Incluir");
        System.out.println("2 - Alterar");
        System.out.println("3 - Excluir");
        System.out.println("4 - Exibir por ID");
        System.out.println("5 - Exibir todos");
        System.out.println("0 - Sair");
        System.out.print("\nOpção: ");
    }
    
    // Lê a opção escolhida pelo usuário.
    private static int lerOpcao() {
        try {
            return Integer.parseInt(sc.nextLine());
        }
        catch (NumberFormatException e) {
            System.err.println("Erro ao ler a opção: a entrada deve ser um NÚMERO.");
            return -1;
        }
    }
    
    // Recebe a opção escolhida pelo usuário e a processa
    // direcionando para a funcionalidade escolhida.
    private static void processarOpcao(int opcao) throws SQLException {
        switch (opcao) {
            case 1 -> incluirPessoa();
            case 2 -> alterarPessoa();
            case 3 -> excluirPessoa();
            case 4 -> exibirPessoa();
            case 5 -> exibirTodos();
            case 0 -> {
                System.out.println("\nEncerrando o sistema...");
                System.out.println("Tchau! Até a próxima :)\n");

                break;
            }
            default -> System.err.println("\nOPÇÃO INVÁLIDA. Escolha um número entre 0(zero) e 5 (cinco).\n");
        }
    }
    
    // Permite que o usuário escolha o tipo de pessoa a ser trabalhada,
    // que pode ser física ou jurídica.
    private static char escolherTipoPessoa() {
        System.out.print("\nPessoa física (F) ou jurídica (J)? ");
        return sc.nextLine().toUpperCase().charAt(0);        
    }
    
    // Inclui dados de uma pessoa no banco de dados.
    private static void incluirPessoa() throws SQLException {
        char tipoPessoa = escolherTipoPessoa();
        
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Logradouro: ");
        String logradouro = sc.nextLine();
        System.out.print("Cidade: ");
        String cidade = sc.nextLine();
        System.out.print("Estado: ");
        String estado = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();
        System.out.print("E-mail: ");
        String email = sc.nextLine();
        
        switch (tipoPessoa) {
            case 'F' -> {
                System.out.print("CPF (apenas números): ");
                String cpf = sc.nextLine();
                PessoaFisica pf = new PessoaFisica(0, nome, logradouro, cidade, estado, telefone, email, cpf);
                pfDAO.incluir(pf);
            }
            case 'J' -> {
                System.out.print("CNPJ (apenas números): ");
                String cnpj = sc.nextLine();
                PessoaJuridica pj = new PessoaJuridica(0, nome, logradouro, cidade, estado, telefone, email, cnpj);
                pjDAO.incluir(pj);
                
                System.out.println("\nPessoa jurídica incluída com sucesso! ID: " + pj.getId());
            }
            default -> System.err.println("\nTipo de pessoa inválido.\n");
        }
    }
    
    // Altera dados de uma pessoa no banco de dados.
    private static void alterarPessoa() throws SQLException {
        char tipoPessoa = escolherTipoPessoa();
        
        System.out.print("Informe o ID: ");
        int id = Integer.parseInt(sc.nextLine());        
        
        switch (tipoPessoa) {
            case 'F' -> {
                PessoaFisica pf = pfDAO.getPessoa(id);
                
                if (pf != null) {
                    System.out.println("\nDados atuais:");
                    pf.exibir();
                    
                    System.out.println("\nInforme os novos dados:");
                    System.out.print("Nome: ");
                    pf.setNome(sc.nextLine());
                    System.out.print("Logradouro: ");
                    pf.setLogradouro(sc.nextLine());
                    System.out.print("Cidade: ");
                    pf.setCidade(sc.nextLine());
                    System.out.print("Estado: ");
                    pf.setEstado(sc.nextLine());
                    System.out.print("Telefone: ");
                    pf.setTelefone(sc.nextLine());
                    System.out.print("E-mail: ");
                    pf.setEmail(sc.nextLine());
                    System.out.print("CPF: ");
                    pf.setCpf(sc.nextLine());
                    
                    pfDAO.alterar(pf);
                    System.out.println("\nDados da pessoa física alterados com sucesso!\n");
                }
                else {
                    System.err.println("\nPessoa física não encontrada.\n");
                }
            }
            case 'J' -> {
                PessoaJuridica pj = pjDAO.getPessoa(id);
                
                if (pj != null) {
                    System.out.println("Informe os novos dados:");
                    System.out.print("Nome: ");
                    pj.setNome(sc.nextLine());
                    System.out.print("Logradouro: ");
                    pj.setLogradouro(sc.nextLine());
                    System.out.print("Cidade: ");
                    pj.setCidade(sc.nextLine());
                    System.out.print("Estado: ");
                    pj.setEstado(sc.nextLine());
                    System.out.print("Telefone: ");
                    pj.setTelefone(sc.nextLine());
                    System.out.print("E-mail: ");
                    pj.setEmail(sc.nextLine());
                    System.out.print("CPF: ");
                    pj.setCnpj(sc.nextLine());
                    
                    pjDAO.alterar(pj);
                    System.out.println("\nDados da pessoa jurídica alterados com sucesso!\n");
                }
                else {
                    System.err.println("\nPessoa jurídica não encontrada.\n");
                }
            }
            default -> System.err.println("\nTipo de pessoa inválido.\n");
        }        
    }
    
    // Exclui dados de uma pessoa no banco de dados.
    private static void excluirPessoa() throws SQLException {
        char tipoPessoa = escolherTipoPessoa();
        
        System.out.print("Informe o ID: ");
        int id = Integer.parseInt(sc.nextLine());
        
        switch(tipoPessoa) {
            case 'F' -> {
                PessoaFisica pf = pfDAO.getPessoa(id);
                
                if (pf != null) {
                    System.out.println("\nDados da pessoa a ser excluída:");
                    System.out.println("----------------------");
                    pf.exibir();
                    System.out.println("----------------------");
                    
                    System.out.print("\nATENÇÃO! Tem certeza que deseja excluir (S/N)? ");
                    switch (sc.nextLine().toUpperCase().charAt(0)) {
                        case 'S' -> {
                            pfDAO.excluir(id);
                            System.out.println("Pessoa física excluída com sucesso!\n");
                        }
                        case 'N' -> System.out.println("Pessoa física não excluída. Registro mantido.\n");
                        default -> System.err.println("Opção desconhecida. Registro mantido.\n");
                    }
                }
                else {
                    System.err.println("Pessoa física não encontrada.\n");
                }
            }
            case 'J' -> {
                PessoaJuridica pj = pjDAO.getPessoa(id);
                
                if (pj != null) {
                    System.out.println("\nDados da pessoa a ser excluída:");
                    System.out.println("----------------------");
                    pj.exibir();
                    System.out.println("----------------------");
                    
                    System.out.print("\nATENÇÃO! Tem certeza que deseja excluir (S/N)? ");
                    switch (sc.nextLine().toUpperCase().charAt(0)) {
                        case 'S' -> {
                            pjDAO.excluir(id);
                            System.out.println("Pessoa jurídica excluída com sucesso!\n");
                        }
                        case 'N' -> System.out.println("Pessoa física não excluída. Registro mantido.\n");
                        default -> System.err.println("Opção desconhecida. Registro mantido.\n");
                    }
                }
                else {
                    System.err.println("Pessoa jurídica não encontrada.\n");
                }
            }
            default -> System.err.println("\nTipo de pessoa inválido.\n");
        }
    }
    
    // Exibe os dados de uma pessoa de acordo com seu ID.
    private static void exibirPessoa() throws SQLException {
        char tipoPessoa = escolherTipoPessoa();
        
        System.out.print("Informe o ID: ");
        int id = Integer.parseInt(sc.nextLine());
        
        switch(tipoPessoa) {
            case 'F' -> {
                PessoaFisica pf = pfDAO.getPessoa(id);
                
                if (pf != null) {
                    System.out.println("\n----------------------");
                    pf.exibir();
                    System.out.println("----------------------\n");
                }
                else {
                    System.err.println("\nPessoa física não encontrada.\n");
                }
            }
            case 'J' -> {
                PessoaJuridica pj = pjDAO.getPessoa(id);
                
                if (pj != null) {
                    pj.exibir();
                }
                else {
                    System.err.println("\nPessoa jurídica não encontrada.\n");
                }
            }
            default -> System.err.println("\nTipo de pessoa inválido.\n");
        }
    }
    
    // Exibe os dados de todas as pessoas do tipo selecionado.
    private static void exibirTodos() throws SQLException {
        char tipoPessoa = escolherTipoPessoa();
        
        switch (tipoPessoa) {
            case 'F' -> {
                List<PessoaFisica> pessoas = pfDAO.getPessoas();
                
                if (pessoas.isEmpty()) {
                    System.out.println("\nNenhuma pessoa física cadastrada.\n");
                }
                else {
                    System.out.println("\nPessoas físicas cadastradas:");
                    for (PessoaFisica pessoa : pessoas) {
                        System.out.println("\n----------------------");
                        pessoa.exibir();
                    }
                }
            }
            case 'J' -> {
                List<PessoaJuridica> pessoas = pjDAO.getPessoas();
                
                if (pessoas.isEmpty()) {
                    System.out.println("\nNenhuma pessoa jurídica cadastrada.\n");
                }
                else {
                    System.out.println("\nPessoas juridicas cadastradas:");
                    for (PessoaJuridica pessoa : pessoas) {
                        System.out.println("\n----------------------");
                        pessoa.exibir();
                    }
                }
            }
            default -> System.err.println("\nTipo de pessoa inválido.\n");
        }
    }
}
