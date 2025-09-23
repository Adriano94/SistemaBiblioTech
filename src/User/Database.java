package User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável pela conexão com o banco de dados e criação das tabelas
 */
public class Database {
    // Configurações de conexão com o banco de dados MySQL no Aiven
    private static final String HOST = "mysql-sistemabibliotech-sistemabibliotech.c.aivencloud.com";
    private static final String PORT = "28070";
    private static final String DATABASE = "Books";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_8EGyk7UoW9VWr43ykkC";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + 
                                    "?sslMode=REQUIRED&useSSL=true&useUnicode=true&characterEncoding=UTF-8";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver MySQL carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL não encontrado!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Cria as tabelas necessárias se não existirem
     */
    public static void criarTabelasSeNecessario() {
        criarTabelaUsers();
        criarTabelaBooksHistory();
    }
    
    /**
     * Cria a tabela de usuários se não existir
     */
    private static void criarTabelaUsers() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "cargo VARCHAR(50) NOT NULL, " +
                    "cpf VARCHAR(14) UNIQUE NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "senha VARCHAR(100) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✅ Tabela 'users' verificada/criada com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar tabela users: " + e.getMessage());
        }
    }
    
    /**
     * Cria a tabela de histórico de livros se não existir
     */
    private static void criarTabelaBooksHistory() {
        String sql = "CREATE TABLE IF NOT EXISTS books_history (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "user_nome VARCHAR(100) NOT NULL, " +
                    "user_email VARCHAR(100) NOT NULL, " +
                    "book_id INT NOT NULL, " +
                    "book_titulo VARCHAR(255) NOT NULL, " +
                    "book_isbn VARCHAR(20) NOT NULL, " +
                    "acao VARCHAR(20) NOT NULL, " + // RESERVA ou DEVOLUCAO
                    "data_acao TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id))";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✅ Tabela 'books_history' verificada/criada com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar tabela books_history: " + e.getMessage());
        }
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Conexão com o banco estabelecida com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Falha na conexão: " + e.getMessage());
        }
    }
}