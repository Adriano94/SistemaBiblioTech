package User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável pela conexão com o banco de dados para o sistema de reservas
 * Usa as mesmas credenciais do sistema de gerenciamento
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
     * @return Objeto Connection para interagir com o banco
     * @throws SQLException Se ocorrer um erro na conexão
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
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