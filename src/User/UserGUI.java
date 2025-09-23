package User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interface gráfica principal do sistema de reservas para usuários autenticados
 */
public class UserGUI extends JFrame {
    private Usuario usuario;
    private BibliotecaService service;
    private JTextArea outputArea;

    public UserGUI(Usuario usuario) {
        this.usuario = usuario;
        this.service = new BibliotecaService(usuario);
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema de Reservas - Usuário: " + usuario.getNome());
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cabeçalho com informações do usuário
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Bem-vindo, " + usuario.getNome() + " (" + usuario.getCargo() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel emailLabel = new JLabel("Email: " + usuario.getEmail());
        emailLabel.setForeground(Color.GRAY);
        
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userInfoPanel.add(welcomeLabel);
        userInfoPanel.add(Box.createHorizontalStrut(20));
        userInfoPanel.add(emailLabel);
        
        JButton logoutButton = new JButton("Sair");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sair();
            }
        });
        
        headerPanel.add(userInfoPanel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Área de saída
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel de controles
        JPanel controlPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        // Linha 1: Pesquisa
        JButton pesquisarButton = new JButton("Pesquisar Livros");
        JButton listarButton = new JButton("Listar Disponíveis");
        JButton historicoButton = new JButton("Meu Histórico");

        // Linha 2: Ações
        JButton reservarButton = new JButton("Reservar Livro");
        JButton devolverButton = new JButton("Devolver Livro");
        JButton limparButton = new JButton("Limpar Tela");

        controlPanel.add(pesquisarButton);
        controlPanel.add(listarButton);
        controlPanel.add(historicoButton);
        controlPanel.add(reservarButton);
        controlPanel.add(devolverButton);
        controlPanel.add(limparButton);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        // Listeners dos botões
        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarLivros();
            }
        });

        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarLivrosDisponiveis();
            }
        });

        historicoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarMeuHistorico();
            }
        });

        reservarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reservarLivro();
            }
        });

        devolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                devolverLivro();
            }
        });

        limparButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
            }
        });

        add(mainPanel);
    }

    private void pesquisarLivros() {
        String[] opcoes = {"TITULO", "AUTOR", "ISBN"};
        String tipo = (String) JOptionPane.showInputDialog(this, 
            "Pesquisar por:", "Pesquisa de Livros",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (tipo != null) {
            String termo = JOptionPane.showInputDialog(this, "Digite o termo de pesquisa:");
            if (termo != null) {
                outputArea.append("🔍 Pesquisando por " + tipo + ": '" + termo + "'...\n\n");
                List<Livro> livros = service.pesquisarLivros(tipo, termo, outputArea);
                
                if (livros.isEmpty()) {
                    outputArea.append("Nenhum livro encontrado.\n");
                } else {
                    for (Livro livro : livros) {
                        outputArea.append(livro.toString() + "\n");
                    }
                    outputArea.append("\nTotal encontrado: " + livros.size() + " livros\n");
                }
            }
        }
    }

    /**
     * listar livros disponíveis
     */
    private void listarLivrosDisponiveis() {
        outputArea.append("📚 Listando todos os livros disponíveis...\n\n");
        
        // Usar o novo método que retorna TODOS os livros
        List<Livro> todosLivros = service.listarTodosLivros(outputArea);
        
        // Filtrar apenas os disponíveis
        List<Livro> disponiveis = todosLivros.stream()
            .filter(Livro::isDisponivel)
            .toList();
            
        if (disponiveis.isEmpty()) {
            outputArea.append("Nenhum livro disponível no momento.\n");
        } else {
            for (Livro livro : disponiveis) {
                outputArea.append(livro.toString() + "\n");
            }
            outputArea.append("\n📊 Resumo:\n");
            outputArea.append("• Total de livros no acervo: " + todosLivros.size() + "\n");
            outputArea.append("• Livros disponíveis: " + disponiveis.size() + "\n");
            outputArea.append("• Livros indisponíveis: " + (todosLivros.size() - disponiveis.size()) + "\n");
        }
    }

    private void consultarMeuHistorico() {
        outputArea.append("📋 Meu histórico de reservas...\n\n");
        List<String> historico = service.consultarMeuHistorico(outputArea);
        
        if (historico.isEmpty()) {
            outputArea.append("Nenhum registro no seu histórico.\n");
        } else {
            for (String registro : historico) {
                outputArea.append(registro + "\n");
            }
            outputArea.append("\nTotal de registros: " + historico.size() + "\n");
        }
    }

    private void reservarLivro() {
        String idStr = JOptionPane.showInputDialog(this, "Digite o ID do livro que deseja reservar:");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int livroId = Integer.parseInt(idStr);
                service.reservarLivro(livroId, outputArea);
            } catch (NumberFormatException e) {
                outputArea.append("❌ ID inválido! Digite um número.\n");
            }
        }
    }

    private void devolverLivro() {
        String idStr = JOptionPane.showInputDialog(this, "Digite o ID do livro que deseja devolver:");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int livroId = Integer.parseInt(idStr);
                service.devolverLivro(livroId, outputArea);
            } catch (NumberFormatException e) {
                outputArea.append("❌ ID inválido! Digite um número.\n");
            }
        }
    }

    private void sair() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente sair do sistema?", "Confirmação",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginGUI().setVisible(true);
            this.dispose();
        }
    }
}