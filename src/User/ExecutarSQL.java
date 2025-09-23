package User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ExecutarSQL {
    public static void main(String[] args) {
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            
            System.out.println("🔄 Configurando banco de dados...");
            
            // 1. Criar tabela users
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nome VARCHAR(100) NOT NULL, " +
                "cargo VARCHAR(50) NOT NULL, " +
                "cpf VARCHAR(14) UNIQUE NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "senha VARCHAR(100) NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            System.out.println("✅ Tabela 'users' criada/verificada");
            
            // 2. Criar tabela books_history (sim, pode ter underscore!)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS books_history (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "user_nome VARCHAR(100) NOT NULL, " +
                "user_email VARCHAR(100) NOT NULL, " +
                "book_id INT NOT NULL, " +
                "book_titulo VARCHAR(255) NOT NULL, " +
                "book_isbn VARCHAR(20) NOT NULL, " +
                "acao VARCHAR(20) NOT NULL, " +
                "data_acao TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            System.out.println("✅ Tabela 'books_history' criada/verificada");
            
            // 3. Inserir usuários de teste
            String[] usuarios = {
                "('João Silva', 'Estudante', '123.456.789-00', 'joao@email.com', 'senha123')",
                "('Maria Santos', 'Funcionário', '987.654.321-00', 'maria@email.com', 'senha123')",
                "('Pedro Oliveira', 'Responsável', '111.222.333-44', 'pedro@email.com', 'senha123')"
            };
            
            for (String usuario : usuarios) {
                try {
                    String sql = "INSERT IGNORE INTO users (nome, cargo, cpf, email, senha) VALUES " + usuario;
                    int rows = stmt.executeUpdate(sql);
                    if (rows > 0) {
                        System.out.println("✅ Usuário inserido: " + usuario.split(",")[0].replace("('", ""));
                    }
                } catch (Exception e) {
                    System.out.println("ℹ️ Usuário já existe: " + usuario.split(",")[0].replace("('", ""));
                }
            }
            
            // 4. Verificar tabelas criadas (CORRIGIDO)
            System.out.println("\n📊 Verificando tabelas criadas:");
            
            // Método 1: Listar todas as tabelas e filtrar
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            while (rs.next()) {
                String tableName = rs.getString(1);
                if (tableName.startsWith("user") || tableName.startsWith("book") || tableName.startsWith("books")) {
                    System.out.println("📁 Tabela: " + tableName);
                }
            }
            
            // Método alternativo: Consultar INFORMATION_SCHEMA
            System.out.println("\n📋 Lista completa de tabelas:");
            rs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'Books'");
            while (rs.next()) {
                System.out.println("📄 " + rs.getString("TABLE_NAME"));
            }
            
            // 5. Verificar usuários inseridos
            System.out.println("\n👥 Usuários cadastrados:");
            rs = stmt.executeQuery("SELECT id, nome, cargo, email FROM users");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                 " | Nome: " + rs.getString("nome") +
                                 " | Cargo: " + rs.getString("cargo") +
                                 " | Email: " + rs.getString("email"));
            }
            
            conn.close();
            System.out.println("\n🎉 Configuração concluída com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}