package User;

/**
 * Classe que representa um usuário do sistema de reservas
 */
public class Usuario {
    private int id;
    private String nome;

    /**
     * Construtor da classe Usuario
     * @param id ID do usuário
     * @param nome Nome do usuário
     */
    public Usuario(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Métodos getters
    public int getId() { return id; }
    public String getNome() { return nome; }

    /**
     * Retorna uma representação em string do usuário
     * @return String formatada com informações do usuário
     */
    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome='" + nome + "'}";
    }
}