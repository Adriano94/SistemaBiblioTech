import javax.swing.SwingUtilities;

/**
 * Classe principal que inicia a aplicação BiblioTech
 * Contém o método main, ponto de entrada do programa
 */
public class Main {
    /**
     * Método principal que inicia a aplicação
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Inicia a interface gráfica na thread do EDT (Event Dispatch Thread)
        // Isso é necessário para aplicações Swing
        SwingUtilities.invokeLater(() -> {
            BibliotecaGUI gui = new BibliotecaGUI();
            gui.setVisible(true); // Torna a janela visível
            System.out.println("🚀 Sistema BiblioTech iniciado com sucesso!");
        });
    }
}