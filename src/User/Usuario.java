package User;

/**
 * Classe que representa um usuário do sistema
 */
public class Usuario {
    private int id;
    private String nome;
    private String cargo;
    private String cpf;
    private String email;
    private String senha;

    public Usuario(int id, String nome, String cargo, String cpf, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }

    @Override
    public String toString() {
        return String.format("Usuário: %s (%s) - %s", nome, email, cargo);
    }
}