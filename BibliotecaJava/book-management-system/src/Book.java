// Classe que representa um livro
public class Book {
    private String title;   // Título do livro
    private String author;  // Autor do livro
    private String isbn;    // ISBN do livro
    private int year;       // Ano de publicação

    // Construtor do livro
    public Book(String title, String author, String isbn, int year) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
    }

    // Métodos getters e setters para acessar e modificar os atributos
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // Exibe as informações do livro no console
    public void displayInfo() {
        System.out.println("Título: " + title);
        System.out.println("Autor: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Ano: " + year);
    }
}