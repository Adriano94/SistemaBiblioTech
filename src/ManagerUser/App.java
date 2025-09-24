package ManagerUser;

/**
 * Classe principal do gerenciador de usuários
 */
public class App {
    public static void main(String[] args) {
        System.out.println("=== GERENCIADOR DE USUÁRIOS - SISTEMA BIBLIOTECH ===");
        Database.testConnection();
        Database.criarTabelaSeNecessario();
        
        // Iniciar interface gráfica
        UserManagerGUI.main(args);
    }
}