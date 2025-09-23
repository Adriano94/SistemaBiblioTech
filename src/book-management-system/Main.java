package book.management.system;

/**
 * Classe principal do sistema de gerenciamento de livros
 * Ponto de entrada da aplicação
 */
public class Main {
    /**
     * Método principal que inicia a aplicação
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        // Testa a conexão com o banco de dados
        System.out.println("🚀 Iniciando Sistema de Gerenciamento de Livros...");
        DatabaseConnection.testConnection();
        
        // Inicia a interface gráfica
        BibliotecaGUI.main(args);
    }
}