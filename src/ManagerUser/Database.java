package ManagerUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável pela conexão com o banco de dados
 * Reutiliza as mesmas configurações dos outros sistemas
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

    /**
     * Bloco estático para carregar o driver MySQL automaticamente
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver MySQL carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL não encontrado!");
            e.printStackTrace();
        }
    }

    /**
     * Estabelece uma conexão com o banco de dados
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Garante que a tabela de usuários existe
     */
    public static void criarTabelaSeNecessario() {
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
     * Testa a conexão com o banco de dados
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Conexão com o banco estabelecida com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Falha na conexão: " + e.getMessage());
        }
    }
}