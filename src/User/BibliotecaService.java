import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BibliotecaService {

    public List<Livro> consultarLivros() throws SQLException {
        List<Livro> livros = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM livros")) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getBoolean("reservado")
                );
                livros.add(livro);
            }
        }

        // imprime bonitinho usando o toString()
        System.out.println("Livros disponíveis:");
        for (Livro l : livros) {
            System.out.println(l);
        }

        return livros;
    }

    public void reservarLivro(int idLivro, String usuario) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE livros SET reservado = TRUE WHERE id = ?")) {

            ps.setInt(1, idLivro);
            ps.executeUpdate();

            registrarHistorico(usuario, "Reservou", idLivro, conn);
        }
    }

    public void devolverLivro(int idLivro, String usuario) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE livros SET reservado = FALSE WHERE id = ?")) {

            ps.setInt(1, idLivro);
            ps.executeUpdate();

            registrarHistorico(usuario, "Devolveu", idLivro, conn);
        }
    }

    public List<String> consultarHistorico() throws SQLException {
        List<String> historico = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM historico ORDER BY data DESC")) {

            while (rs.next()) {
                String registro = rs.getString("usuario") + " " +
                        rs.getString("acao") + " livro " +
                        rs.getInt("livro_id") + " em " +
                        rs.getTimestamp("data");
                historico.add(registro);
            }
        }

        // imprime o histórico
        System.out.println("Histórico de ações:");
        for (String h : historico) {
            System.out.println(h);
        }

        return historico;
    }

    private void registrarHistorico(String usuario, String acao, int idLivro, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO historico(usuario, acao, livro_id) VALUES(?, ?, ?)")) {
            ps.setString(1, usuario);
            ps.setString(2, acao);
            ps.setInt(3, idLivro);
            ps.executeUpdate();
        }
    }
}
