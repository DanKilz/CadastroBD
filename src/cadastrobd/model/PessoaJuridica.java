package cadastrobd.model;

public class PessoaJuridica extends Pessoa {
    // ATRIBUTOS
    private String cnpj;
    
    // CONSTRUTORES
    public PessoaJuridica() {}

    public PessoaJuridica(int id, String nome, String logradouro, String cidade, String estado, String telefone, String email, String cnpj) {
        super(id, nome, logradouro, cidade, estado, telefone, email);
        this.cnpj = cnpj;
    }
    
    // GETTERS E SETTERS
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    // MÉTODOS

    @Override
    public void exibir() {
        super.exibir();
        System.out.println("CNPJ: " + getCnpj());
    }    
}
