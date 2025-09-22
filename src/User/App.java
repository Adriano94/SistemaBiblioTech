package User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // Testar conexão
        System.out.println("=== SISTEMA DE RESERVAS DE LIVROS ===");
        Database.testConnection();
        
        BibliotecaService service = new BibliotecaService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Consultar livros disponíveis");
            System.out.println("2 - Reservar livro");
            System.out.println("3 - Devolver livro");
            System.out.println("4 - Consultar histórico");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            
            try {
                int opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar buffer

                switch (opcao) {
                    case 1:
                        service.consultarLivros();
                        break;
                        
                    case 2:
                        System.out.print("ID do livro: ");
                        int idReserva = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Seu nome: ");
                        String nomeReserva = scanner.nextLine();
                        service.reservarLivro(idReserva, nomeReserva);
                        break;
                        
                    case 3:
                        System.out.print("ID do livro: ");
                        int idDevolucao = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Seu nome: ");
                        String nomeDevolucao = scanner.nextLine();
                        service.devolverLivro(idDevolucao, nomeDevolucao);
                        break;
                        
                    case 4:
                        service.consultarHistorico();
                        break;
                        
                    case 0:
                        System.out.println("Saindo...");
                        scanner.close();
                        return;
                        
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (SQLException e) {
                System.out.println("❌ Erro no banco de dados: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Erro: " + e.getMessage());
                scanner.nextLine(); // Limpar buffer em caso de erro
            }
        }
    }
}