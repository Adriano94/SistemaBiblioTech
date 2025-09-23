package book.management.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTextArea;

/**
 * Classe responsável por gerenciar as operações de CRUD (Create, Read, Update, Delete)
 * para a entidade Book no banco de dados
 * Esta classe é o coração do sistema de gerenciamento de livros
 */
public class BookManager {

    /**
     * Construtor da classe BookManager
     * Garante que a tabela books exista no banco ao inicializar
     */
    public BookManager() {
        // Cria a tabela se não existir
        DatabaseConnection.createTableIfNotExists();
    }

    /**
     * Adiciona um novo livro ao banco de dados ou atualiza a quantidade se o ISBN já existir
     * @param book Objeto Book contendo os dados do livro
     */
    public void addBook(Book book) {
        // Primeiro verifica se o livro já existe pelo ISBN
        String checkSql = "SELECT id, quantidade_total, quantidade_disponivel, reservados FROM books WHERE isbn = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setString(1, book.getIsbn());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Livro já existe, atualiza a quantidade
                int quantidadeTotalAtual = rs.getInt("quantidade_total");
                int quantidadeDisponivelAtual = rs.getInt("quantidade_disponivel");
                int reservadosAtual = rs.getInt("reservados");
                
                int novaQuantidadeTotal = quantidadeTotalAtual + book.getQuantidadeTotal();
                int novaQuantidadeDisponivel = quantidadeDisponivelAtual + book.getQuantidadeDisponivel();
                int novosReservados = reservadosAtual + book.getReservados();
                boolean todosReservados = (novaQuantidadeDisponivel == 0);
                
                String updateSql = "UPDATE books SET quantidade_total = ?, quantidade_disponivel = ?, reservados = ?, todos_reservados = ? WHERE isbn = ?";
                
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, novaQuantidadeTotal);
                    updateStmt.setInt(2, novaQuantidadeDisponivel);
                    updateStmt.setInt(3, novosReservados);
                    updateStmt.setBoolean(4, todosReservados);
                    updateStmt.setString(5, book.getIsbn());
                    
                    updateStmt.executeUpdate();
                    System.out.println("✅ Quantidade do livro atualizada com sucesso!");
                }
            } else {
                // Livro não existe, insere um novo
                String insertSql = "INSERT INTO books (title, author, isbn, year, quantidade_total, quantidade_disponivel, reservados, todos_reservados) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, book.getTitle());
                    insertStmt.setString(2, book.getAuthor());
                    insertStmt.setString(3, book.getIsbn());
                    insertStmt.setString(4, book.getYear());
                    insertStmt.setInt(5, book.getQuantidadeTotal());
                    insertStmt.setInt(6, book.getQuantidadeDisponivel());
                    insertStmt.setInt(7, book.getReservados());
                    insertStmt.setBoolean(8, book.isTodosReservados());
                    
                    insertStmt.executeUpdate();
                    System.out.println("✅ Livro adicionado com sucesso!");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao adicionar/atualizar livro: " + e.getMessage());
        }
    }

    /**
     * Lista todos os livros do banco em uma área de texto gráfica
     * @param outputArea Área de texto onde os resultados serão exibidos
     */
    public void listBooksGUI(JTextArea outputArea) {
        // Comando SQL para selecionar todos os livros
        String sql = "SELECT id, title, author, isbn, year, quantidade_total, quantidade_disponivel, reservados, todos_reservados FROM books";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            outputArea.append("📚 Lista de Livros:\n\n");
            // Itera sobre os resultados e os adiciona à área de texto
            while (rs.next()) {
                outputArea.append("ID: " + rs.getLong("id") +
                        " | Título: " + rs.getString("title") +
                        " | Autor: " + rs.getString("author") +
                        " | ISBN: " + rs.getString("isbn") +
                        " | Ano: " + rs.getString("year") +
                        " | Total: " + rs.getInt("quantidade_total") +
                        " | Disponível: " + rs.getInt("quantidade_disponivel") +
                        " | Reservados: " + rs.getInt("reservados") +
                        " | Status: " + (rs.getBoolean("todos_reservados") ? "Indisponível" : "Disponível") + "\n");
            }

        } catch (SQLException e) {
            outputArea.append("❌ Erro ao listar livros: " + e.getMessage() + "\n");
        }
    }

    /**
     * Pesquisa livros no banco de dados com base em diferentes critérios
     * @param searchType Tipo de pesquisa (1=Título, 2=Autor, 3=ISBN)
     * @param searchTerm Termo a ser pesquisado
     * @param outputArea Área de texto onde os resultados serão exibidos
     */
    public void searchBooks(int searchType, String searchTerm, JTextArea outputArea) {
        String sql = "";
        // Define o comando SQL com base no tipo de pesquisa
        switch (searchType) {
            case 1: // Pesquisa por título
                sql = "SELECT id, title, author, isbn, year, quantidade_total, quantidade_disponivel, reservados, todos_reservados FROM books WHERE title LIKE ?";
                break;
            case 2: // Pesquisa por autor
                sql = "SELECT id, title, author, isbn, year, quantidade_total, quantidade_disponivel, reservados, todos_reservados FROM books WHERE author LIKE ?";
                break;
            case 3: // Pesquisa por ISBN
                sql = "SELECT id, title, author, isbn, year, quantidade_total, quantidade_disponivel, reservados, todos_reservados FROM books WHERE isbn LIKE ?";
                break;
            default:
                outputArea.append("❌ Tipo de pesquisa inválido!\n");
                return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define o parâmetro da pesquisa (com % para busca parcial)
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            // Itera sobre os resultados
            while (rs.next()) {
                found = true;
                outputArea.append("ID: " + rs.getLong("id") +
                        " | Título: " + rs.getString("title") +
                        " | Autor: " + rs.getString("author") +
                        " | ISBN: " + rs.getString("isbn") +
                        " | Ano: " + rs.getString("year") +
                        " | Disponível: " + rs.getInt("quantidade_disponivel") + "/" + rs.getInt("quantidade_total") + "\n");
            }
            
            // Mensagem se nenhum livro for encontrado
            if (!found) {
                outputArea.append("❌ Nenhum livro encontrado.\n");
            }

        } catch (SQLException e) {
            outputArea.append("❌ Erro na pesquisa: " + e.getMessage() + "\n");
        }
    }

    /**
     * Exclui um livro do banco de dados com base no ISBN
     * @param isbn ISBN do livro a ser excluído
     */
    public void deleteBookByIsbn(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Livro removido com sucesso!");
            } else {
                System.out.println("⚠️ ISBN não encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao excluir: " + e.getMessage());
        }
    }

    /**
     * Exclui um livro do banco de dados com base no ID
     * @param id ID do livro a ser excluído
     */
    public void deleteBookById(long id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Livro removido com sucesso!");
            } else {
                System.out.println("⚠️ ID não encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao excluir: " + e.getMessage());
        }
    }

    /**
     * Obtém as informações de um livro pelo ISBN
     * @param isbn ISBN do livro
     * @return Objeto Book com as informações ou null se não encontrado
     */
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT title, author, isbn, year, quantidade_total, quantidade_disponivel, reservados, todos_reservados FROM books WHERE isbn = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Book(
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("year"),
                    rs.getInt("quantidade_total"),
                    rs.getInt("quantidade_disponivel"),
                    rs.getInt("reservados"),
                    rs.getBoolean("todos_reservados")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar livro: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Atualiza as informações de estoque de um livro
     * @param isbn ISBN do livro a ser atualizado
     * @param total Nova quantidade total
     * @param disponivel Nova quantidade disponível
     * @param reservados Novo número de reservas
     */
    public void atualizarEstoque(String isbn, int total, int disponivel, int reservados) {
        // Calcula se todos os livros estão reservados
        boolean todosReservados = (disponivel == 0);
        
        String sql = "UPDATE books SET quantidade_total = ?, quantidade_disponivel = ?, reservados = ?, todos_reservados = ? WHERE isbn = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, total);
            stmt.setInt(2, disponivel);
            stmt.setInt(3, reservados);
            stmt.setBoolean(4, todosReservados);
            stmt.setString(5, isbn);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Estoque atualizado!");
            } else {
                System.out.println("⚠️ ISBN não encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar estoque: " + e.getMessage());
        }
    }

    /**
     * Obtém as informações de estoque de um livro
     * @param isbn ISBN do livro
     * @return Array com [quantidadeTotal, quantidadeDisponivel, reservados]
     */
    public int[] obterEstoque(String isbn) {
        String sql = "SELECT quantidade_total, quantidade_disponivel, reservados FROM books WHERE isbn = ?";
        int[] estoque = new int[3]; // [total, disponivel, reservados]

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                estoque[0] = rs.getInt("quantidade_total");
                estoque[1] = rs.getInt("quantidade_disponivel");
                estoque[2] = rs.getInt("reservados");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao obter estoque: " + e.getMessage());
        }

        return estoque;
    }
}