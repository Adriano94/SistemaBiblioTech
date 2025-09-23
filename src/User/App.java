package User;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal do sistema de reservas de livros
 * Interface de linha de comando para os usuários finais
 */
public class App {
    /**
     * Método principal que inicia o sistema de reservas
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        // Testa a conexão com o banco de dados
        System.out.println("=== SISTEMA DE RESERVAS DE LIVROS ===");
        Database.testConnection();
        
        BibliotecaService service = new BibliotecaService();
        Scanner scanner = new Scanner(System.in);

        // Loop principal do menu
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
                scanner.nextLine(); // Limpa o buffer do teclado

                switch (opcao) {
                    case 1:
                        // Consulta todos os livros disponíveis
                        System.out.println("\nCarregando lista de livros...");
                        List<Livro> livros = service.consultarLivros();
                        if (livros.isEmpty()) {
                            System.out.println("Nenhum livro encontrado no acervo.");
                        }
                        break;
                        
                    case 2:
                        // Reserva um livro
                        System.out.print("\nID do livro: ");
                        int idReserva = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Seu nome: ");
                        String nomeReserva = scanner.nextLine();
                        service.reservarLivro(idReserva, nomeReserva);
                        break;
                        
                    case 3:
                        // Devolve um livro
                        System.out.print("\nID do livro: ");
                        int idDevolucao = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Seu nome: ");
                        String nomeDevolucao = scanner.nextLine();
                        service.devolverLivro(idDevolucao, nomeDevolucao);
                        break;
                        
                    case 4:
                        // Consulta o histórico
                        System.out.println("\nCarregando histórico...");
                        List<String> historico = service.consultarHistorico();
                        if (historico.isEmpty()) {
                            System.out.println("Nenhum registro no histórico.");
                        }
                        break;
                        
                    case 0:
                        // Sai do sistema
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
                scanner.nextLine(); // Limpa o buffer em caso de erro
            }
        }
    }
}