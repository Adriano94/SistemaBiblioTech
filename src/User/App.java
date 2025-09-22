import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        BibliotecaService service = new BibliotecaService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Sistema Biblioteca ---");
            System.out.println("1 - Consultar livros");
            System.out.println("2 - Reservar livro");
            System.out.println("3 - Devolver livro");
            System.out.println("4 - Consultar histórico");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            int opc = sc.nextInt();
            sc.nextLine(); // limpar buffer

            try {
                switch (opc) {
                    case 1:
                        List<Livro> livros = service.consultarLivros();
                        if (livros.isEmpty()) {
                            System.out.println("Nenhum livro encontrado.");
                        }
                        break;

                    case 2:
                        System.out.print("Informe ID do livro: ");
                        int id1 = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Seu nome: ");
                        String nome1 = sc.nextLine();
                        service.reservarLivro(id1, nome1);
                        System.out.println("Livro reservado com sucesso!");
                        break;

                    case 3:
                        System.out.print("Informe ID do livro: ");
                        int id2 = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Seu nome: ");
                        String nome2 = sc.nextLine();
                        service.devolverLivro(id2, nome2);
                        System.out.println("Livro devolvido com sucesso!");
                        break;

                    case 4:
                        List<String> historico = service.consultarHistorico();
                        if (historico.isEmpty()) {
                            System.out.println("Nenhum registro no histórico.");
                        }
                        break;

                    case 0:
                        System.out.println("Saindo...");
                        sc.close();
                        return;

                    default:
                        System.out.println("Opção inválida");
                }
            } catch (SQLException e) {
                System.out.println("Erro de banco de dados: " + e.getMessage());
            }
        }
    }
}
