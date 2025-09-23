package book.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados MySQL no Aiven
 * Esta classe é usada pelo sistema de gerenciamento de livros
 */
public class DatabaseConnection {
    // Configurações de conexão com o banco de dados MySQL no Aiven
    private static final String HOST = "mysql-sistemabibliotech-sistemabibliotech.c.aivencloud.com";
    private static final String PORT = "28070";
    private static final String DATABASE = "Books";
    private static final String USER = "avnadmin"; // Usuário padrão do Aiven
    private static final String PASSWORD = "AVNS_8EGyk7UoW9VWr43ykkC";
    
    // URL de conexão JDBC com parâmetros SSL
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + 
                                    "?sslMode=REQUIRED&useSSL=true&useUnicode=true&characterEncoding=UTF-8";

    /**
     * Bloco estático para carregar o driver MySQL automaticamente
     * Executado quando a classe é carregada pela JVM
     */
    static {
        try {
            // Carrega o driver JDBC do MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver MySQL carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL não encontrado!");
            e.printStackTrace();
        }
    }

    /**
     * Estabelece uma conexão com o banco de dados MySQL
     * @return Objeto Connection para interagir com o banco
     * @throws SQLException Se ocorrer um erro na conexão
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /**
     * Cria a tabela 'books' se ela não existir no banco
     * Este método é executado automaticamente ao iniciar a aplicação
     */
    public static void createTableIfNotExists() {
        // Comando SQL para criar a tabela books com campos de controle de estoque
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "author VARCHAR(255) NOT NULL, " +
                    "isbn VARCHAR(20) NOT NULL UNIQUE, " +
                    "year VARCHAR(10) NOT NULL, " +
                    "quantidade_total INT NOT NULL DEFAULT 1, " +
                    "quantidade_disponivel INT NOT NULL DEFAULT 1, " +
                    "reservados INT NOT NULL DEFAULT 0, " +
                    "todos_reservados BOOLEAN NOT NULL DEFAULT FALSE, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Executa o comando SQL
            stmt.executeUpdate(sql);
            System.out.println("✅ Tabela 'books' verificada/criada com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao criar tabela: " + e.getMessage());
        }
    }

    /**
     * Testa a conexão com o banco de dados
     * Útil para verificar se as credenciais estão corretas
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Conexão com o banco estabelecida com sucesso!");
        } catch (SQLException e) {
            System.err.println("❌ Falha na conexão: " + e.getMessage());
        }
    }
}