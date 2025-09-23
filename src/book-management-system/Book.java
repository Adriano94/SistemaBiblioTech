package book.management.system;

/**
 * Classe que representa um livro no sistema de gerenciamento de biblioteca
 * Contém informações básicas do livro e dados de estoque/reservas
 */
public class Book {
    private String title;
    private String author;
    private String isbn;
    private String year;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private int reservados;
    private boolean todosReservados;

    /**
     * Construtor completo para criar um livro com todas as informações
     * @param title Título do livro
     * @param author Autor do livro
     * @param isbn ISBN do livro
     * @param year Ano de publicação
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
     * Construtor simplificado para criar um livro com valores padrão
     * @param title Título do livro
     * @param author Autor do livro
     * @param isbn ISBN do livro
     * @param year Ano de publicação
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
     * Exibe as informações do livro no console (para debug)
     */
    public void displayInfo() {
        System.out.println("📖 Informações do Livro:");
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
        return "Book{title='" + title + "', author='" + author + "', isbn='" + isbn + 
               "', year='" + year + "', total=" + quantidadeTotal + 
               ", disponivel=" + quantidadeDisponivel + ", reservados=" + reservados + 
               ", todosReservados=" + todosReservados + "}";
    }
}