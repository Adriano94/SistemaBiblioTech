import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe que implementa a interface gráfica do sistema BiblioTech
 * Inclui funcionalidades para gerenciamento de estoque e reservas
 */
public class BibliotecaGUI extends JFrame {
    private BookManager manager;
    private JTextArea outputArea;
    private JPanel buttonPanel; // Declarado como variável de classe para acesso em todos os métodos

    /**
     * Construtor da interface gráfica
     * Inicializa os componentes e configura a janela
     */
    public BibliotecaGUI() {
        manager = new BookManager();
        initComponents();
    }

    /**
     * Inicializa os componentes da interface gráfica
     */
    private void initComponents() {
        // Configurações básicas da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema BiblioTech - Interface Gráfica");
        setSize(800, 600);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Painel principal com layout de borda
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Área de texto para exibir resultados
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel de botões com layout de grade (agora é uma variável de classe)
        buttonPanel = new JPanel(new GridLayout(1, 6, 5, 5)); // Alterado para 6 colunas

        // Criação dos botões
        JButton addButton = new JButton("Adicionar Livro");
        JButton listButton = new JButton("Listar Livros");
        JButton searchButton = new JButton("Pesquisar Livro");
        JButton deleteButton = new JButton("Excluir Livro");
        JButton estoqueButton = new JButton("Gerenciar Estoque");
        JButton exitButton = new JButton("Sair");

        // Adiciona os botões ao painel
        buttonPanel.add(addButton);
        buttonPanel.add(listButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(estoqueButton);
        buttonPanel.add(exitButton);

        // Adiciona o painel de botões à parte inferior da janela
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Define as ações dos botões
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarLivro();
            }
        });

        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarLivros();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarLivro();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirLivro();
            }
        });
        
        estoqueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerenciarEstoque();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Adiciona o painel principal à janela
        add(mainPanel);
    }

    /**
 * Abre um diálogo para adicionar um novo livro com informações de estoque
 */
private void adicionarLivro() {
    // Cria um painel com campos de entrada
    JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
    
    // Declara os campos de texto no escopo do método
    JTextField titleField = new JTextField();
    JTextField authorField = new JTextField();
    JTextField isbnField = new JTextField();
    JTextField yearField = new JTextField();
    JTextField totalField = new JTextField("1");
    JTextField disponivelField = new JTextField("1");
    JTextField reservadosField = new JTextField("0");

    // Adiciona rótulos e campos ao painel
    panel.add(new JLabel("Título:"));
    panel.add(titleField);
    panel.add(new JLabel("Autor:"));
    panel.add(authorField);
    panel.add(new JLabel("ISBN:"));
    panel.add(isbnField);
    panel.add(new JLabel("Ano (yyyy-mm-dd):"));
    panel.add(yearField);
    panel.add(new JLabel("Quantidade Total:"));
    panel.add(totalField);
    panel.add(new JLabel("Quantidade Disponível:"));
    panel.add(disponivelField);
    panel.add(new JLabel("Reservados:"));
    panel.add(reservadosField);

    // Exibe o diálogo de entrada
    int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Livro", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    // Processa os dados se o usuário clicou em OK
    if (result == JOptionPane.OK_OPTION) {
        try {
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();
            String year = yearField.getText();
            int total = Integer.parseInt(totalField.getText());
            int disponivel = Integer.parseInt(disponivelField.getText());
            int reservados = Integer.parseInt(reservadosField.getText());
            boolean todosReservados = (disponivel == 0);

            // Valida se todos os campos foram preenchidos
            if (!title.isEmpty() && !author.isEmpty() && !isbn.isEmpty() && !year.isEmpty()) {
                Book book = new Book(title, author, isbn, year, total, disponivel, reservados, todosReservados);
                
                // Verifica se o livro já existe
                Book livroExistente = manager.getBookByIsbn(isbn);
                if (livroExistente != null) {
                    // Pergunta ao usuário se deseja adicionar mais exemplares
                    int confirm = JOptionPane.showConfirmDialog(this, 
                        "Já existe um livro com este ISBN:\n" +
                        "Título: " + livroExistente.getTitle() + "\n" +
                        "Autor: " + livroExistente.getAuthor() + "\n" +
                        "Quantidade atual: " + livroExistente.getQuantidadeTotal() + "\n\n" +
                        "Deseja adicionar " + total + " exemplares a este livro?",
                        "Livro já existe", 
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        manager.addBook(book);
                        outputArea.append("✅ " + total + " exemplares adicionados ao livro: " + title + "\n");
                    } else {
                        outputArea.append("❌ Operação cancelada pelo usuário.\n");
                    }
                } else {
                    manager.addBook(book);
                    outputArea.append("✅ Livro adicionado: " + title + " (Estoque: " + total + " total, " + disponivel + " disponível)\n");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores numéricos inválidos para estoque!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    /**
     * Lista todos os livros do banco de dados
     */
    private void listarLivros() {
        outputArea.setText(""); // Limpa a área de texto
        outputArea.append("📚 Listando livros...\n\n");
        manager.listBooksGUI(outputArea);
    }

    /**
     * Abre um diálogo para pesquisar livros
     */
    private void pesquisarLivro() {
        String[] options = {"Título", "Autor", "ISBN"};
        int choice = JOptionPane.showOptionDialog(this, "Pesquisar por:", "Pesquisar Livro",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice >= 0) {
            String searchTerm = JOptionPane.showInputDialog(this, "Digite o termo de pesquisa:");
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                outputArea.setText(""); // Limpa a área de texto
                outputArea.append("🔍 Resultados da pesquisa por " + options[choice] + ": '" + searchTerm + "'\n\n");
                manager.searchBooks(choice + 1, searchTerm, outputArea);
            }
        }
    }

    /**
     * Abre um diálogo para excluir um livro
     */
    private void excluirLivro() {
        String[] options = {"Por ISBN", "Por ID"};
        int choice = JOptionPane.showOptionDialog(this, "Excluir por:", "Excluir Livro",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) { // Exclusão por ISBN
            String isbn = JOptionPane.showInputDialog(this, "Digite o ISBN do livro a ser excluído:");
            if (isbn != null && !isbn.trim().isEmpty()) {
                manager.deleteBookByIsbn(isbn);
                outputArea.append("🗑️ Tentativa de excluir livro com ISBN: " + isbn + "\n");
            }
        } else if (choice == 1) { // Exclusão por ID
            try {
                String idStr = JOptionPane.showInputDialog(this, "Digite o ID do livro a ser excluído:");
                if (idStr != null && !idStr.trim().isEmpty()) {
                    long id = Long.parseLong(idStr);
                    manager.deleteBookById(id);
                    outputArea.append("🗑️ Tentativa de excluir livro com ID: " + id + "\n");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Gerencia o estoque de um livro específico
     */
    private void gerenciarEstoque() {
        String isbn = JOptionPane.showInputDialog(this, "Digite o ISBN do livro para gerenciar estoque:");
        if (isbn != null && !isbn.trim().isEmpty()) {
            int[] estoqueAtual = manager.obterEstoque(isbn);
            
            if (estoqueAtual[0] > 0) { // Se encontrou o livro
                JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
                JTextField totalField = new JTextField(String.valueOf(estoqueAtual[0]));
                JTextField disponivelField = new JTextField(String.valueOf(estoqueAtual[1]));
                JTextField reservadosField = new JTextField(String.valueOf(estoqueAtual[2]));
                
                panel.add(new JLabel("Quantidade Total:"));
                panel.add(totalField);
                panel.add(new JLabel("Quantidade Disponível:"));
                panel.add(disponivelField);
                panel.add(new JLabel("Reservados:"));
                panel.add(reservadosField);
                
                int result = JOptionPane.showConfirmDialog(this, panel, "Gerenciar Estoque", 
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int total = Integer.parseInt(totalField.getText());
                        int disponivel = Integer.parseInt(disponivelField.getText());
                        int reservados = Integer.parseInt(reservadosField.getText());
                        
                        manager.atualizarEstoque(isbn, total, disponivel, reservados);
                        outputArea.append("✅ Estoque atualizado para ISBN: " + isbn + "\n");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Valores numéricos inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Livro não encontrado com esse ISBN!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}