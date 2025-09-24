package ManagerUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interface gráfica para gerenciamento de usuários
 * Design similar aos outros sistemas do projeto
 */
public class UserManagerGUI extends JFrame {
    private UserManagerService service;
    private JTextArea outputArea;

    public UserManagerGUI() {
        service = new UserManagerService();
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Gerenciador de Usuários - Sistema BiblioTech");
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cabeçalho
        JLabel tituloLabel = new JLabel("👥 Gerenciador de Usuários", JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(tituloLabel, BorderLayout.NORTH);

        // Área de texto para resultados
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        JButton cadastrarButton = new JButton("➕ Cadastrar Usuário");
        JButton alterarButton = new JButton("✏️ Alterar Usuário");
        JButton excluirButton = new JButton("🗑️ Excluir Usuário");
        JButton pesquisarButton = new JButton("🔍 Pesquisar Usuário");
        JButton listarButton = new JButton("📋 Listar Todos");
        JButton limparButton = new JButton("🧹 Limpar Tela");

        // Estilizar botões
        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        cadastrarButton.setFont(buttonFont);
        alterarButton.setFont(buttonFont);
        excluirButton.setFont(buttonFont);
        pesquisarButton.setFont(buttonFont);
        listarButton.setFont(buttonFont);
        limparButton.setFont(buttonFont);

        buttonPanel.add(cadastrarButton);
        buttonPanel.add(alterarButton);
        buttonPanel.add(excluirButton);
        buttonPanel.add(pesquisarButton);
        buttonPanel.add(listarButton);
        buttonPanel.add(limparButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Listeners dos botões
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarUsuario();
            }
        });

        alterarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarUsuario();
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirUsuario();
            }
        });

        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarUsuario();
            }
        });

        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarUsuarios();
            }
        });

        limparButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
                outputArea.append("✅ Tela limpa. Pronto para novas operações.\n");
            }
        });

        add(mainPanel);
    }

    private void cadastrarUsuario() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        JTextField nomeField = new JTextField();
        JComboBox<String> cargoCombo = new JComboBox<>(new String[]{"Estudante", "Funcionário", "Responsável"});
        JTextField cpfField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField senhaField = new JPasswordField();

        panel.add(new JLabel("Nome Completo:"));
        panel.add(nomeField);
        panel.add(new JLabel("Cargo:"));
        panel.add(cargoCombo);
        panel.add(new JLabel("CPF:"));
        panel.add(cpfField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Senha:"));
        panel.add(senhaField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Cadastrar Novo Usuário", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            String cargo = (String) cargoCombo.getSelectedItem();
            String cpf = cpfField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(senhaField.getPassword());

            // Validação básica
            if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario novoUsuario = new Usuario(nome, cargo, cpf, email, senha);
            service.cadastrarUsuario(novoUsuario, outputArea);
        }
    }

    private void alterarUsuario() {
        String idStr = JOptionPane.showInputDialog(this, "Digite o ID do usuário a ser alterado:");
        if (idStr == null || idStr.trim().isEmpty()) {
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Usuario usuario = service.buscarUsuarioPorId(id, outputArea);

            if (usuario == null) {
                outputArea.append("❌ Usuário com ID " + id + " não encontrado.\n");
                return;
            }

            JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

            JTextField nomeField = new JTextField(usuario.getNome());
            JComboBox<String> cargoCombo = new JComboBox<>(new String[]{"Estudante", "Funcionário", "Responsável"});
            cargoCombo.setSelectedItem(usuario.getCargo());
            JTextField cpfField = new JTextField(usuario.getCpf());
            JTextField emailField = new JTextField(usuario.getEmail());
            JPasswordField senhaField = new JPasswordField(usuario.getSenha());

            panel.add(new JLabel("Nome Completo:"));
            panel.add(nomeField);
            panel.add(new JLabel("Cargo:"));
            panel.add(cargoCombo);
            panel.add(new JLabel("CPF:"));
            panel.add(cpfField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Senha:"));
            panel.add(senhaField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Alterar Usuário - ID: " + id, 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String nome = nomeField.getText().trim();
                String cargo = (String) cargoCombo.getSelectedItem();
                String cpf = cpfField.getText().trim();
                String email = emailField.getText().trim();
                String senha = new String(senhaField.getPassword());

                if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Usuario usuarioAlterado = new Usuario(id, nome, cargo, cpf, email, senha);
                service.alterarUsuario(usuarioAlterado, outputArea);
            }

        } catch (NumberFormatException e) {
            outputArea.append("❌ ID inválido! Digite um número.\n");
        }
    }

    private void excluirUsuario() {
        String idStr = JOptionPane.showInputDialog(this, "Digite o ID do usuário a ser excluído:");
        if (idStr == null || idStr.trim().isEmpty()) {
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            
            // Buscar usuário para confirmar
            Usuario usuario = service.buscarUsuarioPorId(id, outputArea);
            if (usuario == null) {
                outputArea.append("❌ Usuário com ID " + id + " não encontrado.\n");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja excluir o usuário?\n" + usuario.toString(),
                "Confirmação de Exclusão", 
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                service.excluirUsuario(id, outputArea);
            }

        } catch (NumberFormatException e) {
            outputArea.append("❌ ID inválido! Digite um número.\n");
        }
    }

    private void pesquisarUsuario() {
        String[] opcoes = {"Nome", "Email", "CPF"};
        String tipo = (String) JOptionPane.showInputDialog(this, 
            "Pesquisar por:", "Pesquisa de Usuários",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (tipo != null) {
            String termo = JOptionPane.showInputDialog(this, "Digite o termo de pesquisa:");
            if (termo != null && !termo.trim().isEmpty()) {
                outputArea.append("🔍 Pesquisando por " + tipo + ": '" + termo + "'...\n\n");
                List<Usuario> usuarios = service.pesquisarUsuarios(tipo, termo, outputArea);

                if (usuarios.isEmpty()) {
                    outputArea.append("Nenhum usuário encontrado.\n");
                } else {
                    for (Usuario usuario : usuarios) {
                        outputArea.append(usuario.toString() + "\n");
                    }
                    outputArea.append("\nTotal encontrado: " + usuarios.size() + " usuários\n");
                }
            }
        }
    }

    private void listarUsuarios() {
        outputArea.append("📋 Listando todos os usuários cadastrados...\n\n");
        List<Usuario> usuarios = service.listarTodosUsuarios(outputArea);

        if (usuarios.isEmpty()) {
            outputArea.append("Nenhum usuário cadastrado no sistema.\n");
        } else {
            for (Usuario usuario : usuarios) {
                outputArea.append(usuario.toString() + "\n");
            }
            outputArea.append("\n=== RESUMO ===\n");
            outputArea.append("Total de usuários: " + usuarios.size() + "\n");
            
            // Estatísticas por cargo
            long estudantes = usuarios.stream().filter(u -> u.getCargo().equals("Estudante")).count();
            long funcionarios = usuarios.stream().filter(u -> u.getCargo().equals("Funcionário")).count();
            long responsaveis = usuarios.stream().filter(u -> u.getCargo().equals("Responsável")).count();
            
            outputArea.append("Estudantes: " + estudantes + "\n");
            outputArea.append("Funcionários: " + funcionarios + "\n");
            outputArea.append("Responsáveis: " + responsaveis + "\n");
        }
    }

    public static void main(String[] args) {
        // Garantir que a tabela existe
        Database.criarTabelaSeNecessario();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserManagerGUI().setVisible(true);
            }
        });
    }
}