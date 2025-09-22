package User;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private int reservados;
    private boolean todosReservados;

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

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public int getQuantidadeTotal() { return quantidadeTotal; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public int getReservados() { return reservados; }
    public boolean isTodosReservados() { return todosReservados; }
    
    public boolean isDisponivel() {
        return quantidadeDisponivel > 0 && !todosReservados;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s por %s | Disponível: %d/%d | Reservados: %d | %s",
                id, titulo, autor, quantidadeDisponivel, quantidadeTotal, 
                reservados, isDisponivel() ? "DISPONÍVEL" : "INDISPONÍVEL");
    }
}