package cadastrobd.model;

public class PessoaFisica extends Pessoa {
    // ATRIBUTOS
    private String cpf;
    
    // CONSTRUTORES
    public PessoaFisica() {}

    public PessoaFisica(int id, String nome, String logradouro, String cidade, String estado, String telefone, String email, String cpf) {
        super(id, nome, logradouro, cidade, estado, telefone, email);
        this.cpf = cpf;
    }

    // GETTERS E SETTERS
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    // MÉTODOS

    @Override
    public void exibir() {
        super.exibir();
        System.out.println("CPF: " + getCpf());
    }    
}
