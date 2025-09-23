package User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interface gráfica para login do usuário
 */
public class LoginGUI extends JFrame {
    private JTextField emailField;
    private JPasswordField senhaField;

    public LoginGUI() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema de Reservas - Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel tituloLabel = new JLabel("Sistema de Reservas de Livros", JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(tituloLabel, BorderLayout.NORTH);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel senhaLabel = new JLabel("Senha:");
        senhaField = new JPasswordField();

        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(senhaLabel);
        formPanel.add(senhaField);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Entrar");
        JButton cancelButton = new JButton("Cancelar");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fazerLogin();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        formPanel.add(new JLabel()); // Espaço vazio
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Informações
        JLabel infoLabel = new JLabel("Use seu email e senha cadastrados", JLabel.CENTER);
        infoLabel.setForeground(Color.GRAY);
        mainPanel.add(infoLabel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter para login
        getRootPane().setDefaultButton(loginButton);
    }

    private void fazerLogin() {
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha email e senha!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Autenticar usuário
        Usuario usuario = BibliotecaService.autenticarUsuario(email, senha);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Login realizado com sucesso!\nBem-vindo, " + usuario.getNome(), 
                                         "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir a interface principal
            new UserGUI(usuario).setVisible(true);
            this.dispose(); // Fechar janela de login
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
            senhaField.setText("");
            emailField.requestFocus();
        }
    }

    public static void main(String[] args) {
        // Criar tabelas se necessário
        Database.criarTabelasSeNecessario();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginGUI().setVisible(true);
            }
        });
    }
}