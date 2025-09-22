package User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaService {

    public List<Livro> consultarLivros() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT id, title, author, isbn, quantidade_total, quantidade_disponivel, reservados, todos_reservados FROM books";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
        }

        System.out.println("\n=== LIVROS DISPONÍVEIS ===");
        for (Livro livro : livros) {
            System.out.println(livro);
        }
        System.out.println("Total de livros: " + livros.size());

        return livros;
    }

    public void reservarLivro(int idLivro, String usuario) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            // Verificar disponibilidade
            String checkSql = "SELECT title, quantidade_disponivel FROM books WHERE id = ? AND quantidade_disponivel > 0 AND todos_reservados = FALSE";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, idLivro);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String titulo = rs.getString("title");
                int disponivel = rs.getInt("quantidade_disponivel");

                if (disponivel <= 0) {
                    throw new SQLException("❌ Livro '" + titulo + "' não está disponível para reserva");
                }

                // Atualizar estoque
                String updateSql = "UPDATE books SET quantidade_disponivel = quantidade_disponivel - 1, reservados = reservados + 1 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, idLivro);
                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    // Registrar no histórico
                    registrarHistorico(usuario, "RESERVOU", idLivro, titulo, conn);
                    conn.commit();
                    System.out.println("✅ Livro '" + titulo + "' reservado com sucesso para " + usuario);
                } else {
                    throw new SQLException("❌ Não foi possível reservar o livro");
                }
            } else {
                throw new SQLException("❌ Livro não encontrado ou indisponível");
            }

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public void devolverLivro(int idLivro, String usuario) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            // Verificar se o livro foi reservado
            String checkSql = "SELECT title, reservados FROM books WHERE id = ? AND reservados > 0";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, idLivro);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String titulo = rs.getString("title");
                int reservados = rs.getInt("reservados");

                if (reservados <= 0) {
                    throw new SQLException("❌ Este livro não tinha reservas pendentes");
                }

                // Atualizar estoque
                String updateSql = "UPDATE books SET quantidade_disponivel = quantidade_disponivel + 1, reservados = reservados - 1 WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, idLivro);
                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    // Registrar no histórico
                    registrarHistorico(usuario, "DEVOLVEU", idLivro, titulo, conn);
                    conn.commit();
                    System.out.println("✅ Livro '" + titulo + "' devolvido com sucesso por " + usuario);
                } else {
                    throw new SQLException("❌ Não foi possível devolver o livro");
                }
            } else {
                throw new SQLException("❌ Livro não encontrado ou não tinha reservas");
            }

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<String> consultarHistorico() throws SQLException {
        List<String> historico = new ArrayList<>();
        criarTabelaHistorico();

        String sql = "SELECT usuario, acao, livro_id, livro_titulo, data FROM historico ORDER BY data DESC LIMIT 20";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String registro = String.format("[%s] %s %s livro ID %d (%s)",
                    rs.getTimestamp("data"),
                    rs.getString("usuario"),
                    rs.getString("acao"),
                    rs.getInt("livro_id"),
                    rs.getString("livro_titulo")
                );
                historico.add(registro);
            }
        }

        System.out.println("\n=== HISTÓRICO ===");
        for (String registro : historico) {
            System.out.println(registro);
        }
        System.out.println("Total de registros: " + historico.size());

        return historico;
    }

    private void registrarHistorico(String usuario, String acao, int livroId, String livroTitulo, Connection conn) throws SQLException {
        criarTabelaHistorico();
        
        String sql = "INSERT INTO historico (usuario, acao, livro_id, livro_titulo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, acao);
            stmt.setInt(3, livroId);
            stmt.setString(4, livroTitulo);
            stmt.executeUpdate();
        }
    }

    private void criarTabelaHistorico() {
        String sql = "CREATE TABLE IF NOT EXISTS historico (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "usuario VARCHAR(100) NOT NULL, " +
                    "acao VARCHAR(20) NOT NULL, " +
                    "livro_id INT NOT NULL, " +
                    "livro_titulo VARCHAR(255) NOT NULL, " +
                    "data TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar tabela de histórico: " + e.getMessage());
        }
    }
}