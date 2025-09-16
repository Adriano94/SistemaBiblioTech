// Classe que representa o administrador do sistema
public class Admin {
    private BookManager bookManager; // Gerenciador de livros

    // Construtor recebe o gerenciador de livros
    public Admin(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    // Adiciona um livro usando o gerenciador
    public void addBook(Book book) {
        bookManager.addBook(book);
    }

    // Atualiza um livro usando o gerenciador
    public void updateBook(String isbn, Book updatedBook) {
        bookManager.updateBook(isbn, updatedBook);
    }

    // Remove um livro usando o gerenciador
    public void deleteBook(String isbn) {
        bookManager.deleteBook(isbn);
    }

    // Exibe as informações de um livro usando o gerenciador
    public void displayBook(String isbn) {
        Book book = bookManager.getBook(isbn);
        if (book != null) {
            book.displayInfo();
        } else {
            System.out.println("Livro não encontrado!");
        }
    }
}