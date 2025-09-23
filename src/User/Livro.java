package User;

/**
 * Classe que representa um livro no sistema de reservas
 * Adaptada para trabalhar com a estrutura da tabela 'books'
 */
public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private int reservados;
    private boolean todosReservados;

    /**
     * Construtor da classe Livro
     * @param id ID do livro no banco de dados
     * @param titulo Título do livro
     * @param autor Autor do livro
     * @param isbn ISBN do livro
     * @param quantidadeTotal Quantidade total de exemplares
     * @param quantidadeDisponivel Quantidade disponível para reserva
     * @param reservados Quantidade de exemplares reservados
     * @param todosReservados Indica se todos os exemplares estão reservados
     */
    public Livro(int id, String titulo, String autor, String isbn, 
                 int quantidadeTotal, int quantidadeDisponivel, int reservados, 
                 boolean todosReservados) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.reservados = reservados;
        this.todosReservados = todosReservados;
    }

    // Métodos getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public int getQuantidadeTotal() { return quantidadeTotal; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public int getReservados() { return reservados; }
    public boolean isTodosReservados() { return todosReservados; }
    
    /**
     * Verifica se o livro está disponível para reserva
     * @return true se há exemplares disponíveis, false caso contrário
     */
    public boolean isDisponivel() {
        return quantidadeDisponivel > 0 && !todosReservados;
    }

    /**
     * Retorna uma representação em string do livro
     * @return String formatada com informações do livro
     */
    @Override
    public String toString() {
        return String.format("ID: %d | %s por %s | Disponível: %d/%d | Reservados: %d | %s",
                id, titulo, autor, quantidadeDisponivel, quantidadeTotal, 
                reservados, isDisponivel() ? "DISPONÍVEL" : "INDISPONÍVEL");
    }
}