package User;

/**
 * Classe principal do sistema de reservas
 * Agora inicia a interface de login
 */
public class App {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE RESERVAS DE LIVROS ===");
        Database.testConnection();
        Database.criarTabelasSeNecessario();
        
        // Iniciar interface de login
        LoginGUI.main(args);
    }
}