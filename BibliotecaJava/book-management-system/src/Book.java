/**
 * Classe que representa um livro no sistema
 * Contém informações básicas como título, autor, ISBN, ano e informações de estoque
 */
public class Book {
    // Atributos da classe
    private String title;
    private String author;
    private String isbn;
    private String year; // Formato: yyyy-mm-dd
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private int reservados;
    private boolean todosReservados;

    /**
     * Construtor da classe Book
     * @param title Título do livro
     * @param author Autor do livro
     * @param isbn ISBN do livro
     * @param year Ano de publicação (formato yyyy-mm-dd)
     * @param quantidadeTotal Quantidade total de exemplares
     * @param quantidadeDisponivel Quantidade disponível para empréstimo
     * @param reservados Quantidade de exemplares reservados
     * @param todosReservados Indica se todos os exemplares estão reservados
     */
    public Book(String title, String author, String isbn, String year, 
                int quantidadeTotal, int quantidadeDisponivel, int reservados, 
                boolean todosReservados) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.reservados = reservados;
        this.todosReservados = todosReservados;
    }

    /**
     * Construtor simplificado para compatibilidade com versões anteriores
     */
    public Book(String title, String author, String isbn, String year) {
        this(title, author, isbn, year, 1, 1, 0, false);
    }

    // Métodos getters para acessar os atributos
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getYear() { return year; }
    public int getQuantidadeTotal() { return quantidadeTotal; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public int getReservados() { return reservados; }
    public boolean isTodosReservados() { return todosReservados; }

    /**
     * Exibe as informações do livro no console
     */
    public void displayInfo() {
        System.out.println("📖 Livro:");
        System.out.println("Título: " + title);
        System.out.println("Autor: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Ano: " + year);
        System.out.println("Quantidade Total: " + quantidadeTotal);
        System.out.println("Quantidade Disponível: " + quantidadeDisponivel);
        System.out.println("Reservados: " + reservados);
        System.out.println("Todos Reservados: " + (todosReservados ? "Sim" : "Não"));
    }

    /**
     * Retorna uma representação em string do livro
     * @return String formatada com informações do livro
     */
    @Override
    public String toString() {
        return "Livro{" +
                "Título='" + title + '\'' +
                ", Autor='" + author + '\'' +
                ", ISBN='" + isbn + '\'' +
                ", Ano='" + year + '\'' +
                ", QuantidadeTotal=" + quantidadeTotal +
                ", QuantidadeDisponivel=" + quantidadeDisponivel +
                ", Reservados=" + reservados +
                ", TodosReservados=" + todosReservados +
                '}';
    }
}