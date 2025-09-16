import java.util.List;
import java.util.ArrayList;

// Classe responsável por gerenciar a lista de livros
public class BookManager {
    private List<Book> books; // Lista de livros cadastrados

    // Construtor inicializa a lista de livros
    public BookManager() {
        books = new ArrayList<>();
    }

    // Adiciona um novo livro à lista
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Livro cadastrado com sucesso!");
    }

    // Atualiza as informações de um livro pelo ISBN
    public boolean updateBook(String isbn, Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                books.set(i, updatedBook);
                System.out.println("Livro atualizado com sucesso!");
                return true;
            }
        }
        System.out.println("Livro não encontrado!");
        return false;
    }

    // Remove um livro da lista pelo ISBN
    public boolean deleteBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                books.remove(book);
                System.out.println("Livro removido com sucesso!");
                return true;
            }
        }
        System.out.println("Livro não encontrado!");
        return false;
    }

    // Busca e retorna um livro pelo ISBN
    public Book getBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }
}