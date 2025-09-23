package User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

/**
 * Classe de serviço para operações de reserva e devolução de livros
 * Agora com suporte a usuários autenticados e histórico personalizado
 */
public class BibliotecaService {
    private Usuario usuarioLogado;

    public BibliotecaService(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    /**
     * Pesquisa livros por título, autor ou ISBN
     */
    public List<Livro> pesquisarLivros(String tipo, String termo, JTextArea outputArea) {
        List<Livro> livros = new ArrayList<>();
        String sql = "";
        
        switch (tipo.toUpperCase()) {
            case "TITULO":
                sql = "SELECT * FROM books WHERE title LIKE ?";
                break;
            case "AUTOR":
                sql = "SELECT * FROM books WHERE author LIKE ?";
                break;
            case "ISBN":
                sql = "SELECT * FROM books WHERE isbn LIKE ?";
                break;
            default:
                outputArea.append("❌ Tipo de pesquisa inválido!\n");
                return livros;
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + termo + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getInt("quantidade_total"),
                    rs.getInt("quantidade_disponivel"),
                    rs.getInt("reservados"),
                    rs.getBoolean("todos_reservados")
                );
                livros.add(livro);
            }
            
        } catch (SQLException e) {
            outputArea.append("❌ Erro na pesquisa: " + e.getMessage() + "\n");
        }
        
        return livros;
    }

    /**
     * Reserva um livro para o usuário logado
     */
    public boolean reservarLivro(int livroId, JTextArea outputArea) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            // Verificar disponibilidade
            String checkSql = "SELECT title, quantidade_disponivel FROM books WHERE id = ? AND quantidade_disponivel > 0";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, livroId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String titulo = rs.getString("title");
                int disponivel = rs.getInt("quantidade_disponivel");

                if (disponivel <= 0) {
                    outputArea.append("❌ Livro '" + titulo + "' não está disponível para reserva\n");
                    return false;
                }

                // Atualizar estoque
                String updateSql = "UPDATE books SET quantidade_disponivel = quantidade_disponivel - 1, reservados = reservados + 1 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, livroId);
                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    // Registrar no histórico
                    registrarHistorico(conn, livroId, titulo, "RESERVA");
                    conn.commit();
                    outputArea.append("✅ Livro '" + titulo + "' reservado com sucesso!\n");
                    return true;
                }
            } else {
                outputArea.append("❌ Livro não encontrado ou indisponível\n");
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            outputArea.append("❌ Erro ao reservar livro: " + e.getMessage() + "\n");
        } finally {
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true); 
                    conn.close(); 
                } catch (SQLException e) {}
            }
        }
        return false;
    }

    /**
     * Devolve um livro reservado pelo usuário logado
     */
    public boolean devolverLivro(int livroId, JTextArea outputArea) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            // Verificar se o livro existe
            String checkSql = "SELECT title, reservados FROM books WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, livroId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String titulo = rs.getString("title");
                int reservados = rs.getInt("reservados");

                if (reservados <= 0) {
                    outputArea.append("❌ Este livro não tinha reservas pendentes\n");
                    return false;
                }

                // Atualizar estoque
                String updateSql = "UPDATE books SET quantidade_disponivel = quantidade_disponivel + 1, reservados = reservados - 1 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, livroId);
                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    // Registrar no histórico
                    registrarHistorico(conn, livroId, titulo, "DEVOLUCAO");
                    conn.commit();
                    outputArea.append("✅ Livro '" + titulo + "' devolvido com sucesso!\n");
                    return true;
                }
            } else {
                outputArea.append("❌ Livro não encontrado\n");
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            outputArea.append("❌ Erro ao devolver livro: " + e.getMessage() + "\n");
        } finally {
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true); 
                    conn.close(); 
                } catch (SQLException e) {}
            }
        }
        return false;
    }

    /**
     * Consulta o histórico do usuário logado
     */
    public List<String> consultarMeuHistorico(JTextArea outputArea) {
        List<String> historico = new ArrayList<>();
        
        String sql = "SELECT book_titulo, book_isbn, acao, data_acao FROM books_history " +
                    "WHERE user_id = ? ORDER BY data_acao DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioLogado.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String registro = String.format("[%s] %s - Livro: %s (ISBN: %s)",
                    rs.getTimestamp("data_acao").toString(),
                    rs.getString("acao"),
                    rs.getString("book_titulo"),
                    rs.getString("book_isbn")
                );
                historico.add(registro);
            }
            
        } catch (SQLException e) {
            outputArea.append("❌ Erro ao consultar histórico: " + e.getMessage() + "\n");
        }
        
        return historico;
    }

    /**
     * Registra uma ação no histórico
     */
    private void registrarHistorico(Connection conn, int livroId, String livroTitulo, String acao) throws SQLException {
        String sql = "INSERT INTO books_history (user_id, user_nome, user_email, book_id, book_titulo, book_isbn, acao) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Obter ISBN do livro
            String isbnSql = "SELECT isbn FROM books WHERE id = ?";
            try (PreparedStatement isbnStmt = conn.prepareStatement(isbnSql)) {
                isbnStmt.setInt(1, livroId);
                ResultSet rs = isbnStmt.executeQuery();
                String isbn = rs.next() ? rs.getString("isbn") : "N/A";
                
                stmt.setInt(1, usuarioLogado.getId());
                stmt.setString(2, usuarioLogado.getNome());
                stmt.setString(3, usuarioLogado.getEmail());
                stmt.setInt(4, livroId);
                stmt.setString(5, livroTitulo);
                stmt.setString(6, isbn);
                stmt.setString(7, acao);
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Autentica um usuário pelo email e senha
     */
    public static Usuario autenticarUsuario(String email, String senha) {
        String sql = "SELECT id, nome, cargo, cpf, email, senha FROM users WHERE email = ? AND senha = ?";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, senha); // Em um sistema real, usar hash para senhas!
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cargo"),
                    rs.getString("cpf"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erro na autenticação: " + e.getMessage());
        }
        
        return null;
    }
}