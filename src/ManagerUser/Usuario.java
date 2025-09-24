package ManagerUser;

/**
 * Classe que representa um usuário do sistema
 * Reutiliza a estrutura da tabela 'users' criada anteriormente
 */
public class Usuario {
    private int id;
    private String nome;
    private String cargo;
    private String cpf;
    private String email;
    private String senha;

    /**
     * Construtor completo para criar um usuário com todas as informações
     */
    public Usuario(int id, String nome, String cargo, String cpf, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
    }

    /**
     * Construtor para novo usuário (sem ID)
     */
    public Usuario(String nome, String cargo, String cpf, String email, String senha) {
        this(0, nome, cargo, cpf, email, senha);
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }

    // Setters para permitir alterações
    public void setNome(String nome) { this.nome = nome; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }

    /**
     * Valida se os dados obrigatórios estão preenchidos
     */
    public boolean isValid() {
        return nome != null && !nome.trim().isEmpty() &&
               cargo != null && !cargo.trim().isEmpty() &&
               cpf != null && !cpf.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               senha != null && !senha.trim().isEmpty();
    }

    /**
     * Retorna uma representação em string do usuário
     */
    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %s | Cargo: %s | CPF: %s | Email: %s", 
                id, nome, cargo, cpf, email);
    }

    /**
     * Retorna uma representação resumida para exibição em listas
     */
    public String toShortString() {
        return String.format("%s (%s) - %s", nome, email, cargo);
    }
}